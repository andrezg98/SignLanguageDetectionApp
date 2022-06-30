package com.andreaziqing.signlanguagedetectionapp.DetectionGames.Practice;

import com.andreaziqing.signlanguagedetectionapp.DetectionGames.BetweenGamesActivity;
import com.andreaziqing.signlanguagedetectionapp.Detector.DetectorActivity;
import com.andreaziqing.signlanguagedetectionapp.Navigation.NavigationTabsController;
import com.andreaziqing.signlanguagedetectionapp.R;
import com.andreaziqing.signlanguagedetectionapp.Detector.TFLiteInterpreter.Detector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
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
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class FullSecondGame extends DetectorActivity implements View.OnClickListener {

    private static final String FULL_SECOND_GAME = "FullSecondGame";

    Context context;

    TextView mFirstLetter, mSecondLetter, mThirdLetter;
    ImageView mFirstLetterImage, mSecondLetterImage, mThirdLetterImage;
    RelativeLayout mFirstCardLetter, mSecondCardLetter, mThirdCardLetter;

    Button mFirsLettertHint, mSecondLetterHint, mThirdLetterHint;

    TextView[] arrLetter;
    ImageView[] arrLetterImage;
    RelativeLayout[] arrCardLetter;
    Button[] arrButtonLetterHint;

    String[] arrGroupOfLetters;

    Thread cardDetectionThread;
    volatile boolean activityStopped = false;
    volatile boolean threadIsInterrupted = false;

    List<String> abecedary = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
    List<Integer> abecedaryImage = Arrays.asList(R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d,
            R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i, R.drawable.j, R.drawable.k,
            R.drawable.l, R.drawable.m, R.drawable.n, R.drawable.o, R.drawable.p, R.drawable.q, R.drawable.r,
            R.drawable.s, R.drawable.t, R.drawable.u, R.drawable.v, R.drawable.w, R.drawable.x, R.drawable.y,
            R.drawable.z);

    Dictionary<String, Integer> signDictionary = new Hashtable<>();

    public int counter;
    TextView time;
    volatile boolean isTimesOut = false;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MediaPlayer mpCorrect;
    public MediaPlayer mpWrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_second_game);

        counter = 0;
        context = getApplicationContext();

        mpCorrect = MediaPlayer.create(context, R.raw.correct);
        mpWrong = MediaPlayer.create(context, R.raw.wrong);

        initViews();

        // Primeras 3 letras
        // Escoger aleatoriamente tres letras del abecedario
        setThreeRandomLetters(arrCardLetter, false);
    }

    private void initViews() {
        for (int i = 0; i < abecedary.toArray().length; i++) {
            signDictionary.put(abecedary.get(i), abecedaryImage.get(i));
        }

        time = findViewById(R.id.countdown_timer);

        mFirstLetter = findViewById(R.id.first_letter);
        mSecondLetter = findViewById(R.id.second_letter);
        mThirdLetter = findViewById(R.id.third_letter);

        mFirstLetterImage = findViewById(R.id.first_letter_image);
        mSecondLetterImage = findViewById(R.id.second_letter_image);
        mThirdLetterImage = findViewById(R.id.third_letter_image);

        mFirsLettertHint = findViewById(R.id.first_letter_hint);
        mSecondLetterHint = findViewById(R.id.second_letter_hint);
        mThirdLetterHint = findViewById(R.id.third_letter_hint);

        mFirstCardLetter = findViewById(R.id.first_card_letter);
        mSecondCardLetter = findViewById(R.id.second_card_letter);
        mThirdCardLetter = findViewById(R.id.third_card_letter);

        arrLetter = new TextView[]{mFirstLetter, mSecondLetter, mThirdLetter};
        arrLetterImage = new ImageView[]{mFirstLetterImage, mSecondLetterImage, mThirdLetterImage};
        arrCardLetter = new RelativeLayout[]{mFirstCardLetter, mSecondCardLetter, mThirdCardLetter};
        arrButtonLetterHint = new Button[]{mFirsLettertHint, mSecondLetterHint, mThirdLetterHint};

        arrGroupOfLetters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        // El Handler será necesario para actualizar la UI desde el hilo principal
        final Handler handler = new Handler();
        final Handler handler2 = new Handler();
        final Handler handler3 = new Handler();

        // Metodo Runnable que lanzará el hilo principal
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(FULL_SECOND_GAME, "Hilo en background: " + Thread.currentThread().getName());
                synchronized (this) {
                    for (int cycle = 0; cycle < 4; cycle++) {
                        Log.d(FULL_SECOND_GAME, "[" + Thread.currentThread() + "]" + "Ciclo #" + cycle + 1);
                        int letterIdx = 0;
                        class InitTimerRunnable implements Runnable {
                            CountDownTimer countDownTimer;

                            InitTimerRunnable() {}
                            public void run() {
                                Log.d(FULL_SECOND_GAME, "["+ Thread.currentThread()+ "]" + "Iniciando temporizador.");
                                countDownTimer = new CountDownTimer(10000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        time.setText((int) (millisUntilFinished / 1000) + "");
                                        counter++;
                                    }

                                    @Override
                                    public void onFinish() {
                                        isTimesOut = true;
                                    }
                                };
                                countDownTimer.start();
                            }
                        }

                        InitTimerRunnable initTimerRunnable = null;
                        
                        // Para ese grupo aleatorio de 3 letras, vamos chekeando una tras otra:
                        for (TextView letter : arrLetter) {
                            // Iniciamos timer desde este hilo

                            initTimerRunnable = new InitTimerRunnable();
                            handler3.postDelayed(initTimerRunnable, 100);

                            // Mostramos la imagen si el usuario hace click sobre hint durante 3 segundos
                            onClickButtonHint(mFirsLettertHint, 0);
                            onClickButtonHint(mSecondLetterHint, 1);
                            onClickButtonHint(mThirdLetterHint, 2);

                            // 0. Hasta que no se detecte por el usuario la letra que aparezca en pantalla no avanza.
                            while (!checkLetter(letter, activityStopped)) {
                            }

                            if (activityStopped) {
                                return;
                            }
                            // Una vez comprobado que la letra ha sido correctamente detectada:
                            // 1. Definimos metodo Runnable que acepta un parametro (indice de la letra a actualizar)
                            class UpdateCardColorRunnable implements Runnable {
                                int letterIndex;

                                UpdateCardColorRunnable(int idx) {
                                    letterIndex = idx;
                                }

                                public void run() {
                                    Log.d(FULL_SECOND_GAME, "[" + Thread.currentThread() + "]" + "Actualizando tarjeta de letra a color verde.");
                                    // Actualizar aqui UI
                                    arrCardLetter[letterIndex].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8CF5C1")));
                                }
                            }

                            mpCorrect.start();

                            initTimerRunnable.countDownTimer.cancel();
                            handler3.removeCallbacksAndMessages(initTimerRunnable);
                            // 2. Lanzamos el Handler para que actualice la interfaz con el runnable interno declarado
                            handler.post(new UpdateCardColorRunnable(letterIdx));
                            // 3. Avanzamos a la siguiente iteración (letra)
                            letterIdx++;
                        }
                        Log.d(FULL_SECOND_GAME, "[" + Thread.currentThread() + "]" + "Adivinado grupo de 3 letras; generando siguiente...");

                        try {
                            wait(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Una vez se han adivinado las 3 letras; volvemos a generar otras 3 aleatorias desde este hilo
                        class UpdateLetterCardsRunnable implements Runnable {
                            UpdateLetterCardsRunnable() {}
                            public void run() {
                                Log.d(FULL_SECOND_GAME, "[" + Thread.currentThread() + "]" + "Regenerando grupo de 3 letas.");
                                setThreeRandomLetters(arrCardLetter, true);
                            }
                        }
                        if (cycle <= 2) {
                            handler2.post(new UpdateLetterCardsRunnable());
                        } // cycle == 3 seria el ultimo y por tanto no tendriamos que regenerar nada mas

                    }
                }
                Log.i(FULL_SECOND_GAME, "Subproceso terminado");
            }
        };

        // Declaramos hilo que lanza el Runnable declarado previamente
        cardDetectionThread = new Thread(runnable);
        cardDetectionThread.start();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(FULL_SECOND_GAME, "State: " + cardDetectionThread.getState() + "isAlive: " + cardDetectionThread.isAlive());
                if (!cardDetectionThread.isAlive()) {
                    if (!threadIsInterrupted) {
                        Log.d(FULL_SECOND_GAME, "Hilo terminado, pasando a la siguiente actividad.");

                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        Map<String, Object> dataToUpdate = new HashMap<>();
                        dataToUpdate.put("ncgames", FieldValue.increment(1));
                        db.collection("userstats")
                                .document(firebaseUser.getUid())
                                .update(dataToUpdate);

                        Intent intent = new Intent(FullSecondGame.this, BetweenGamesActivity.class);
                        intent.putExtra("previousActivity", FULL_SECOND_GAME);
                        intent.putExtra("state", "SUCCESS");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        FullSecondGame.this.startActivity(intent);

                        timer.cancel();
                    } else {
                        Log.d(FULL_SECOND_GAME, "Hilo interrumpido, cerrando actividad.");
                        timer.cancel();
                    }
                } else if (isTimesOut) {
                    Log.d(FULL_SECOND_GAME, "Times out.");
                    mpWrong.start();

                    Intent intent = new Intent(FullSecondGame.this, BetweenGamesActivity.class);
                    intent.putExtra("previousActivity", FULL_SECOND_GAME);
                    intent.putExtra("state", "FAIL");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    FullSecondGame.this.startActivity(intent);

                    timer.cancel();
                } else {
                    Log.d(FULL_SECOND_GAME, "Sigo esperando a que el hilo termine.");
                }
            }
        }, 500, 500);  // first is delay, second is period
    }

    private void onClickButtonHint(Button buttonHint, int finalLetterIdx) {
        buttonHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostramos la imagen si el usuario hace click sobre hint desde este hilo
                arrLetter[finalLetterIdx].setVisibility(View.INVISIBLE);
                arrButtonLetterHint[finalLetterIdx].setVisibility(View.INVISIBLE);
                arrLetterImage[finalLetterIdx].setVisibility(View.VISIBLE);
                class ShowHintImageRunnable implements Runnable {
                    int letterIndex;

                    ShowHintImageRunnable(int idx) {
                        letterIndex = idx;
                    }

                    public void run() {
                        Log.d(FULL_SECOND_GAME, "["+ Thread.currentThread()+ "]" + "Mostrando la imagen de pista.");
                        // Mostramos la imagen en pantalla durante 3 segundos
                        arrLetter[finalLetterIdx].setVisibility(View.VISIBLE);
                        buttonHint.setVisibility(View.VISIBLE);
                        arrLetterImage[finalLetterIdx].setVisibility(View.GONE);
                    }
                }
                new Handler().postDelayed(new ShowHintImageRunnable(finalLetterIdx), 3000);
            }
        });
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

        mpWrong.release();
        mpWrong = null;

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

        if (json.equals("A")) { // No se ha detectado nada
            return false;
        }

        List<Detector.Recognition> mappedRecognitions = gson.fromJson(json, type);

        if (mappedRecognitions == null) {
            // if the array list is empty, creating a new array list.
            mappedRecognitions = new ArrayList<>();
            return false;
        } else {
            // Log.d(FULL_SECOND_GAME, "Cargado Mapped Recognition: " + mappedRecognitions);
            for (Detector.Recognition result : mappedRecognitions) {
                if (result.getTitle().contentEquals(letter.getText())) {
                    Log.d(FULL_SECOND_GAME, "Reconocida la letra: " + result.getTitle());
                    return true;
                }
            }
        }
        return false;
    }

    private void setThreeRandomLetters(RelativeLayout[] arrCardLetter, boolean refreshCard) {
        String positions = "";
        for (int i = 0; i < arrLetter.length; i++) {
            boolean retryLetter = true;
            while (retryLetter) {
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

        if (count == 0) { super.onBackPressed();
            threadIsInterrupted = true;
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
