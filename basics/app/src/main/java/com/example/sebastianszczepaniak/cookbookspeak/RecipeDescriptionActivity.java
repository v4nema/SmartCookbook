package com.example.sebastianszczepaniak.cookbookspeak;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sebastianszczepaniak.cookbookspeak.models.ApplicationState;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import eu.reply.smartcookbook.recipe.Ingredient;
import eu.reply.smartcookbook.recipe.Recipe;

public class RecipeDescriptionActivity extends Activity implements TextToSpeech.OnInitListener {

    private static final String TAG = "RecipeDescription";

    private static final int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech tts;
    private int currentStep = 0;
    private boolean mIsListening = false;
    private boolean mIsWorking = false;
    private SpeechRecognizer sr = null;
    private Intent recognizerIntent = null;
    private List<TextView> stepTextViews = new ArrayList<>();
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_description);

        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new listener());

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        final Recipe selectedRecipe = ApplicationState.getInstance().getSelectedRecipe();

        prepareViewForRecipe(selectedRecipe);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    private void prepareViewForRecipe(Recipe selectedRecipe) {
        final TextView titleTextView = (TextView) findViewById(R.id.recipe_title);
        titleTextView.setText(selectedRecipe.getName());

        final TextView ingredientsContainer = (TextView) findViewById(R.id.recipe_ingredientsBox);
        final String ingredientsString = prepareIngredientsString(selectedRecipe.getIngredients());
        ingredientsContainer.setText(ingredientsString);

        final LinearLayout methodStepsContainer = (LinearLayout) findViewById(R.id.recipe_steps_container);
        methodStepsContainer.removeAllViews();


        for (int i = 0; i < selectedRecipe.getSteps().size(); i++) {
            final TextView stepTextView = new TextView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int dp_5 = (int) getResources().getDimension(R.dimen.margin_bottom_5);
            params.setMargins(0, 0, 0, dp_5);
            stepTextView.setLayoutParams(params);
            stepTextView.setPadding(dp_5, dp_5, dp_5, dp_5);
            String stepText = (i + 1) + ". " + selectedRecipe.getSteps().get(i);
            stepTextView.setText(stepText);
            stepTextView.setTextColor(Color.parseColor("#000000"));
            methodStepsContainer.addView(stepTextView);
            stepTextViews.add(stepTextView);
        }

        final Button playButton = (Button) findViewById(R.id.play_button);
        nextButton = (Button) findViewById(R.id.next_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView firstStepView = stepTextViews.get(currentStep);
                firstStepView.setBackgroundColor(Color.parseColor("#7fbf7f"));
                speak(firstStepView.getText().toString(), TextToSpeech.QUEUE_FLUSH);
                playButton.setEnabled(Boolean.FALSE);
                playButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setEnabled(Boolean.TRUE);
                currentStep++;


                mIsListening = true;
                if (mIsWorking == false) {
                    sr.startListening(recognizerIntent);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView previousStepView = stepTextViews.get(currentStep - 1);
                previousStepView.setBackground(null);
                final TextView currentStepView = stepTextViews.get(currentStep);
                currentStepView.setBackgroundColor(Color.parseColor("#7fbf7f"));
                speak(currentStepView.getText().toString(), TextToSpeech.QUEUE_FLUSH);
                currentStep++;
                if (currentStep == stepTextViews.size()) {
                    finishReading();
                }
            }
        });
    }

    private void finishReading() {
        nextButton.setEnabled(Boolean.FALSE);
        nextButton.setText(R.string.finish_steps);
        nextButton.setBackgroundColor(Color.parseColor("#7fbf7f"));
        speak(getResources().getString(R.string.finish_step_message), TextToSpeech.QUEUE_ADD);
    }

    private String prepareIngredientsString(List<Ingredient> ingredients) {
        String result = "";
        for (int i = 0; i < ingredients.size(); i++) {
            result += ingredients.get(i).getDescription();
            if (i < ingredients.size() - 1) {
                result += "\n";
            }
        }
        return result;
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (sr != null) {
            sr.stopListening();
            sr.cancel();
            sr.destroy();
        }

        super.onDestroy();
    }

    private void speak(String words, int action) {
        tts.speak(words, action, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            if (ApplicationState.getInstance().getLanguage().equals("en")
                    && tts.isLanguageAvailable(Locale.ENGLISH) == TextToSpeech.LANG_AVAILABLE)
            {
                tts.setLanguage(Locale.ENGLISH);
            }

        } else if (status == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                tts = new TextToSpeech(this, this);
            } else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }


    @Override
    protected void onPause() {
        mIsWorking = false;
        if (mIsListening)
            sr.stopListening();

        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mIsListening && !mIsWorking)
            sr.startListening(recognizerIntent);

        super.onResume();
    }

    class listener implements RecognitionListener {
        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech");
            //mStatusText.setText("onReadyForSpeech");
            mIsWorking = true;
        }

        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
//            mStatusText.setText("onBeginningOfSpeech");
            mIsWorking = true;
        }

        public void onRmsChanged(float rmsdB) {
            Log.d(TAG, "onRmsChanged: " + Float.toString(rmsdB));
        }

        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived");
//            mStatusText.setText("onBufferReceived");
        }

        public void onEndOfSpeech() {
            Log.d(TAG, "onEndofSpeech");
//            mStatusText.setText("onEndofSpeech");
        }

        public void onError(int error) {
            Log.d(TAG, "error " + error);

            String errorStr = "Unknown error";
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    errorStr = "ERROR_AUDIO";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    errorStr = "ERROR_CLIENT";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    errorStr = "ERROR_INSUFFICIENT_PERMISSIONS";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    errorStr = "ERROR_NETWORK";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    errorStr = "ERROR_NETWORK_TIMEOUT";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    errorStr = "ERROR_NO_MATCH";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    errorStr = "ERROR_RECOGNIZER_BUSY";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    errorStr = "ERROR_SERVER";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    errorStr = "ERROR_SPEECH_TIMEOUT";
                    break;
            }

//            mStatusText.setText(errorStr);

            mIsWorking = false;
            if (mIsListening)
                sr.startListening(recognizerIntent);
        }

        public void onResults(Bundle results) {
            Log.d(TAG, "onResults");

            ArrayList thingsYouSaid = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            if (!tts.isSpeaking() && thingsYouSaid.size() > 0) {
                Log.d(TAG, " thingsYouSaid.size=" + thingsYouSaid.size());

                String outValue = "";

                for (int i = 0; i < thingsYouSaid.size(); i++) {
                    if (thingsYouSaid.get(i).toString().length() < 10)
                        outValue += thingsYouSaid.get(i); // + "\n";
                }

//                mText.setText(mText.getText().toString()+outValue+" ");
                System.out.println(outValue);

                if (outValue.toLowerCase().contains("next")) {
                    final TextView previousStepView = stepTextViews.get(currentStep - 1);
                    previousStepView.setBackground(null);
                    if (currentStep == stepTextViews.size()) {
                        mIsListening = false;
                        sr.stopListening();

                        finishReading();
                    } else {
                        final TextView currentStepView = stepTextViews.get(currentStep);
                        currentStepView.setBackgroundColor(Color.parseColor("#7fbf7f"));
                        speak(currentStepView.getText().toString(), TextToSpeech.QUEUE_FLUSH);
                        currentStep++;
                    }
                }
                /*
                if (outValue.length() < 10 && outValue.contains("next")) {
                    final TextView previousStepView = stepTextViews.get(currentStep - 1);
                    previousStepView.setBackground(null);
                    final TextView currentStepView = stepTextViews.get(currentStep);
                    currentStepView.setBackgroundColor(Color.parseColor("#7fbf7f"));
                    speak(currentStepView.getText().toString(), TextToSpeech.QUEUE_FLUSH);
                    currentStep++;
                    if (currentStep == stepTextViews.size()) {
                        nextButton.setEnabled(Boolean.FALSE);
                        nextButton.setText("FINISHED");

                        mIsListening = false;
                        sr.stopListening();

                        speak("Congratulations, you have finished cooking your dish. Enjoy your food!", TextToSpeech.QUEUE_ADD);
                    }
                }
                 */
            }

            mIsWorking = false;
            if (mIsListening)
                sr.startListening(recognizerIntent);
        }

        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "onPartialResults");
//            mStatusText.setText("onPartialResults");
        }

        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent " + eventType);
//            mStatusText.setText("onEvent " + eventType);
        }
    }

    public void onClick(View v) {

        if (v.getId() == R.id.actionButton) {
            mIsListening = !mIsListening;

            if (mIsWorking == false) {
                if (mIsListening)
                    sr.startListening(recognizerIntent);
                else
                    sr.stopListening();
            }
//            else
//                mStatusText.setText("start/stop delayed because engine is still working");
        }
    }
}
