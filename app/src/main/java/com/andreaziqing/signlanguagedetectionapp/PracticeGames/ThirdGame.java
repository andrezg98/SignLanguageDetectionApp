package com.andreaziqing.signlanguagedetectionapp.PracticeGames;

import com.andreaziqing.signlanguagedetectionapp.DetectorActivity;
import com.andreaziqing.signlanguagedetectionapp.R;
import com.andreaziqing.signlanguagedetectionapp.TFLiteInterpreter.Detector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;


public class ThirdGame extends DetectorActivity {

    private static final String THIRD_GAME = "Third Game";

    TextView mWord;
    RelativeLayout mCardWord;
    LinearLayout mLetterSpelling;

    TextView[] arrLetter; // En este caso serán las letras de la palabra

    Dictionary<String, Integer> signDictionary = new Hashtable<>();

    Thread cardDetectionThread;
    volatile boolean activityStopped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_game);

        mWord = findViewById(R.id.word);
        mCardWord = findViewById(R.id.card_word);
        mLetterSpelling = findViewById(R.id.letter_spelling);

        // Palabra random
        // Escoger aleatoriamente una palabra del banco de palabras
        setRandomWord(mCardWord,false);

        // TODO: Escoger palabra según temática
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
                Log.d(THIRD_GAME, "Hilo en background: "+ Thread.currentThread().getName());
                synchronized (this) {
                    for (int cycle = 0; cycle < 4; cycle++) {
                        Log.d(THIRD_GAME, "["+ Thread.currentThread()+ "]" + "Ciclo #"+ cycle + 1);
                        int letterIdx = 0;
                        // Para esa palabra, vamos chekeando letra tras letra:
                        for (TextView letter: arrLetter) {
                            // 0. Hasta que no se detecte por el usuario la letra que aparezca en pantalla no avanza.
                            while (!checkLetter(letter, activityStopped)) { }

                            if (activityStopped) {
                                return;
                            }
                            // Una vez comprobado que la letra ha sido correctamente detectada:

                            // Pintamos la letra en pantalla desde este hilo
                            class UpdateSpellingLetterRunnable implements Runnable {
                                UpdateSpellingLetterRunnable() {}
                                public void run() {
                                    Log.d(THIRD_GAME, "["+ Thread.currentThread()+ "]" + "Pintando la letra adivinada.");
                                    // Pintamos la letra en pantalla
                                    letter.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT));
                                    letter.setGravity(Gravity.CENTER);
                                    letter.setTextColor(Color.parseColor("#8CF5C1"));
                                    letter.setTextSize(40);
                                    letter.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                                    mLetterSpelling.addView(letter);
                                }
                            }
                            handler.post(new UpdateSpellingLetterRunnable());

                            // 1. Avanzamos a la siguiente iteración (letra)
                            letterIdx++;

                            // Comprobamos si es la última letra de la palabra
                            if (letterIdx == arrLetter.length) {
                                // 2. Definimos metodo Runnable que acepta un parametro (indice de la letra a actualizar)
                                class UpdateCardColorRunnable implements Runnable {
                                    int letterIndex;
                                    UpdateCardColorRunnable(int idx) { letterIndex = idx; }
                                    public void run() {
                                        Log.d(THIRD_GAME, "["+ Thread.currentThread()+ "]" + "Actualizando tarjeta de letra a color verde.");
                                        // Actualizar aqui UI
                                        mCardWord.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8CF5C1")));
                                    }
                                }
                                // 3. Lanzamos el Handler para que actualice la interfaz con el runnable interno declarado
                                handler2.post(new UpdateCardColorRunnable(letterIdx));
                            }
                        }
                        Log.d(THIRD_GAME, "["+ Thread.currentThread()+ "]" + "Palabra adivinada; generando siguiente...");

                        try {
                            wait(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Una vez se ha adivinado la palabra; volvemos a generar otra palabra desde este hilo
                        class UpdateLetterCardsRunnable implements Runnable {
                            UpdateLetterCardsRunnable() {}
                            public void run() {
                                Log.d(THIRD_GAME, "["+ Thread.currentThread()+ "]" + "Generando nueva palabra.");
                                setRandomWord(mCardWord, true);
                            }
                        }
                        if(cycle <= 2) {
                            handler3.post(new UpdateLetterCardsRunnable());
                        } // cycle == 3 seria el ultimo y por tanto no tendriamos que regenerar nada mas
                    }
                }
                Log.i(THIRD_GAME, "Subproceso terminado");
            }
        };

        // Declaramos hilo que lanza el Runnable declarado previamente
        cardDetectionThread = new Thread(runnable);
        cardDetectionThread.start();

        /*if (!cardDetectionThread.isAlive()) {
            Intent intent = new Intent(this, SecondGame.class);
            intent.putExtra("arrGroupOfLetters", arrGroupOfLetters);
            this.startActivity(intent);
        }*/
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        activityStopped = true;
    }

    @Override
    public synchronized void onStop() {
        super.onStop();

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
            Log.d(THIRD_GAME, "Cargado Mapped Recognition: " + mappedRecognitions);
            for (Detector.Recognition result : mappedRecognitions) {
                if (result.getTitle().contentEquals(letter.getText())) {
                    Log.d(THIRD_GAME, "Reconocida la letra: " + result.getTitle());
                    return true;
                }
            }
        }
        return false;
    }

    private void setRandomWord(RelativeLayout cardWord, boolean refreshCard) {
        //String[] words = new String[]{"SIGNOS", "LENGUAJE", "APRENDER"};
        String[] words = new String[]{"AB"};

        int wordPosition = (int) (Math.random() * (words.length));

        // Pintamos la palabra en pantalla
        mWord.setText(words[wordPosition] + "");

        // Convertimos la palabra en un array de letras
        arrLetter = new TextView[words[wordPosition].length()];

        for (int j = 0; j < words[wordPosition].length(); j ++) {
            TextView newLetter = new TextView(this);
            Log.d(THIRD_GAME, "New letter: " + words[wordPosition].charAt(j));
            newLetter.setText(words[wordPosition].charAt(j) + "");
            arrLetter[j] = newLetter;
        }

        if (refreshCard) {
            // Reseteamos color de la tarjeta a blanco
            cardWord.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            mLetterSpelling.removeAllViews();
        }
    }
}