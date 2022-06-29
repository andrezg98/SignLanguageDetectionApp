package com.andreaziqing.signlanguagedetectionapp.DetectionGames.Lessons;

import com.andreaziqing.signlanguagedetectionapp.Detector.DetectorActivity;
import com.andreaziqing.signlanguagedetectionapp.R;
import com.andreaziqing.signlanguagedetectionapp.Detector.TFLiteInterpreter.Detector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class FirstGame extends DetectorActivity {

    private static final String FIRST_GAME = "FirstGame";

    Context context;

    TextView mFirstLetter, mSecondLetter, mThirdLetter;
    ImageView mFirstLetterImage, mSecondLetterImage, mThirdLetterImage;
    RelativeLayout mFirstCardLetter, mSecondCardLetter, mThirdCardLetter;

    TextView[] arrLetter;
    ImageView[] arrLetterImage;
    RelativeLayout[] arrCardLetter;

    public int mPositionGroup;
    String[] arrGroupOfLetters;

    List<String> abecedary = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
    List<Integer> abecedaryImage = Arrays.asList(R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d,
            R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i, R.drawable.j, R.drawable.k,
            R.drawable.l, R.drawable.m, R.drawable.n, R.drawable.o, R.drawable.p, R.drawable.q, R.drawable.r,
            R.drawable.s, R.drawable.t, R.drawable.u, R.drawable.v, R.drawable.w, R.drawable.x, R.drawable.y,
            R.drawable.z);

    Dictionary<String, Integer> signDictionary = new Hashtable<>();

    Thread cardDetectionThread;
    volatile boolean activityStopped = false;
    volatile boolean threadIsInterrupted = false;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MediaPlayer mpCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_game);

        context = getApplicationContext();

        mpCorrect = MediaPlayer.create(context, R.raw.correct);
        //mpWrong = MediaPlayer.create(context, R.raw.wrong);

        for (int i = 0; i < abecedary.toArray().length; i++) {
            signDictionary.put(abecedary.get(i), abecedaryImage.get(i));
        }

        mFirstLetter = findViewById(R.id.first_letter);
        mSecondLetter = findViewById(R.id.second_letter);
        mThirdLetter = findViewById(R.id.third_letter);

        mFirstLetterImage = findViewById(R.id.first_letter_image);
        mSecondLetterImage = findViewById(R.id.second_letter_image);
        mThirdLetterImage = findViewById(R.id.third_letter_image);

        mFirstCardLetter = findViewById(R.id.first_card_letter);
        mSecondCardLetter = findViewById(R.id.second_card_letter);
        mThirdCardLetter = findViewById(R.id.third_card_letter);

        arrLetter = new TextView[]{mFirstLetter, mSecondLetter, mThirdLetter};
        arrLetterImage = new ImageView[]{mFirstLetterImage, mSecondLetterImage, mThirdLetterImage};
        arrCardLetter = new RelativeLayout[]{mFirstCardLetter, mSecondCardLetter, mThirdCardLetter};

        // Recojo la posición del grupo de letras que ha elegido el usuario
        Bundle bundle = getIntent().getExtras();
        mPositionGroup = bundle.getInt("position");
        setGroupOfLetters(mPositionGroup);

        // Primeras 3 letras
        // Escoger aleatoriamente tres letras (en función del grupo de letras que el usuario haya escogido)
        setThreeRandomLetters(arrCardLetter,false);
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        // El Handler será necesario para actualizar la UI desde el hilo principal
        final Handler handler = new Handler();
        final Handler handler2 = new Handler();

        // Metodo Runnable que lanzará el hilo principal
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(FIRST_GAME, "Hilo en background: "+ Thread.currentThread().getName());
                synchronized (this) {
                    for (int cycle = 0; cycle < 4; cycle++) {
                        Log.d(FIRST_GAME, "["+ Thread.currentThread()+ "]" + "Ciclo #"+ cycle + 1);
                        int letterIdx = 0;
                        // Para ese grupo aleatorio de 3 letras, vamos chekeando una tras otra:
                        for (TextView letter: arrLetter) {
                            // 0. Hasta que no se detecte por el usuario la letra que aparezca en pantalla no avanza.
                            while (!checkLetter(letter, activityStopped)) { }

                            if (activityStopped) {
                                return;
                            }
                            // Una vez comprobado que la letra ha sido correctamente detectada:
                            // 1. Definimos metodo Runnable que acepta un parametro (indice de la letra a actualizar)
                            class UpdateCardColorRunnable implements Runnable {
                                int letterIndex;
                                UpdateCardColorRunnable(int idx) { letterIndex = idx; }
                                public void run() {
                                    Log.d(FIRST_GAME, "["+ Thread.currentThread()+ "]" + "Actualizando tarjeta de letra a color verde.");
                                    // Actualizar aqui UI
                                    arrCardLetter[letterIndex].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8CF5C1")));
                                }
                            }

                            mpCorrect.start();

                            // 2. Lanzamos el Handler para que actualice la interfaz con el runnable interno declarado
                            handler.post(new UpdateCardColorRunnable(letterIdx));
                            // 3. Avanzamos a la siguiente iteración (letra)
                            letterIdx++;
                        }
                        Log.d(FIRST_GAME, "["+ Thread.currentThread()+ "]" + "Adivinado grupo de 3 letras; generando siguiente...");

                        try {
                            wait(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Una vez se han adivinado las 3 letras; volvemos a generar otras 3 aleatorias desde este hilo
                        class UpdateLetterCardsRunnable implements Runnable {
                            UpdateLetterCardsRunnable() {}
                            public void run() {
                                Log.d(FIRST_GAME, "["+ Thread.currentThread()+ "]" + "Regenerando grupo de 3 letas.");
                                setThreeRandomLetters(arrCardLetter, true);
                            }
                        }
                        if(cycle <= 2) {
                            handler2.post(new UpdateLetterCardsRunnable());
                        } // cycle == 3 seria el ultimo y por tanto no tendriamos que regenerar nada mas
                    }
                }
                Log.i(FIRST_GAME, "Subproceso terminado");
            }
        };

        // Declaramos hilo que lanza el Runnable declarado previamente
        cardDetectionThread = new Thread(runnable);
        cardDetectionThread.start();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(FIRST_GAME, "State: " + cardDetectionThread.getState() + "isAlive: " + cardDetectionThread.isAlive());
                if (!cardDetectionThread.isAlive()) {
                    if (!threadIsInterrupted) {
                        Log.d(FIRST_GAME, "Hilo terminado, pasando a la siguiente actividad.");

                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        Map<String, Object> dataToUpdate = new HashMap<>();

                        switch (mPositionGroup) {
                            case 0:
                                dataToUpdate.put("progressl1", "1");
                                break;
                            case 1:
                                dataToUpdate.put("progressl2", "1");
                                break;
                            case 2:
                                dataToUpdate.put("progressl3", "1");
                                break;
                            default:
                                break;
                        }

                        db.collection("userstats")
                                .document(firebaseUser.getUid())
                                .update(dataToUpdate);

                        Intent intent = new Intent(context, SecondGame.class);
                        intent.putExtra("arrGroupOfLetters", arrGroupOfLetters);
                        intent.putExtra("position", mPositionGroup);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        //finish();

                        timer.cancel();
                    } else {
                        Log.d(FIRST_GAME, "Hilo interrumpido, cerrando actividad.");

                        timer.cancel();
                    }
                } else {
                    Log.d(FIRST_GAME, "Sigo esperando a que el hilo termine.");
                }
            }
        }, 500, 500);  // first is delay, second is period
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        activityStopped = true;
    }

    @Override
    public synchronized void onStop() {
        super.onStop();

        mpCorrect.release();
        mpCorrect = null;

        Toast.makeText(this, "Hilo terminado", Toast.LENGTH_SHORT).show();
    }

    private boolean checkLetter(TextView letter, boolean activityStopped) {
        if (activityStopped) {
            return true;
        }
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String json = sharedPreferences.getString("RESULTS", "A");
        Type type = new TypeToken<List<Detector.Recognition>>() {}.getType();

        if(json.equals("A")){ // No se ha detectado nada
            return false;
        }

        List<Detector.Recognition> mappedRecognitions = gson.fromJson(json, type);

        if (mappedRecognitions == null) {
            // if the array list is empty, creating a new array list.
            mappedRecognitions = new ArrayList<>();
            return false;
        } else {
            // Log.d(FIRST_GAME, "Cargado Mapped Recognition: " + mappedRecognitions);
            for (Detector.Recognition result : mappedRecognitions) {
                if (result.getTitle().contentEquals(letter.getText())) {
                    Log.d(FIRST_GAME, "Reconocida la letra: " + result.getTitle());
                    return true;
                }
            }
        }
        return false;
    }

    private void setGroupOfLetters(int position) {
        switch (position) {
            case 0:
                arrGroupOfLetters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
                break;
            case 1:
                arrGroupOfLetters = new String[]{"I", "J", "K", "L", "M", "N", "O", "P"};
                break;
            case 2:
                arrGroupOfLetters = new String[]{"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
                break;

        }
    }

    private void setThreeRandomLetters(RelativeLayout[] arrCardLetter, boolean refreshCard) {
        String positions = "";
        for (int i = 0; i < arrLetter.length; i++) {
            boolean retryLetter = true;
            while(retryLetter) {
                int letterPosition = (int) (Math.random() * (arrGroupOfLetters.length));
                if (!positions.contains(String.valueOf(letterPosition))) {
                    retryLetter = false;
                    arrLetter[i].setText(arrGroupOfLetters[letterPosition]);
                    arrLetterImage[i].setImageResource(signDictionary.get(arrGroupOfLetters[letterPosition]));
                    positions = positions.concat(String.valueOf(letterPosition));

                    if (refreshCard) {
                        // Reseteamos color de la tarjeta a blanco
                        arrCardLetter[i].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                    }
                } else {
                    retryLetter = true;
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            threadIsInterrupted = true;
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}