package com.example.sebastianszczepaniak.cookbookspeak;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allrecipes.Ingredient;
import com.allrecipes.Recipe;
import com.example.sebastianszczepaniak.cookbookspeak.models.ApplicationState;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeDescriptionActivity extends Activity implements TextToSpeech.OnInitListener {

    private static final int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech tts;
    private int currentStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_description);

        final Recipe selectedRecipe = ApplicationState.getInstance().getSelectedRecipe();

        prepareViewForRecipe(selectedRecipe);
        //todo create a button to play/next method
        //todo before speaking the current method, highlight the correct text view

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

        final List<TextView> stepTextViews = new ArrayList<>();
        for (int i=0; i<selectedRecipe.getSteps().size(); i++) {
            final TextView stepTextView = new TextView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int dp_5 = (int) getResources().getDimension(R.dimen.margin_bottom_5);
            params.setMargins(0, 0, 0, dp_5);
            stepTextView.setLayoutParams(params);
            stepTextView.setPadding(dp_5,dp_5,dp_5,dp_5);
            String stepText = (i+1)+". "+selectedRecipe.getSteps().get(i);
            stepTextView.setText(stepText);
            stepTextView.setTextColor(Color.parseColor("#000000"));
            methodStepsContainer.addView(stepTextView);
            stepTextViews.add(stepTextView);
        }

        final Button playButton = (Button) findViewById(R.id.play_button);
        final Button nextButton = (Button) findViewById(R.id.next_button);

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
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView previousStepView = stepTextViews.get(currentStep-1);
                previousStepView.setBackground(null);
                final TextView currentStepView = stepTextViews.get(currentStep);
                currentStepView.setBackgroundColor(Color.parseColor("#7fbf7f"));
                speak(currentStepView.getText().toString(), TextToSpeech.QUEUE_FLUSH);
                currentStep++;
                if(currentStep == stepTextViews.size()){
                    nextButton.setEnabled(Boolean.FALSE);
                    nextButton.setText("FINISHED");
                    speak("Congratulations, you have finished cooking your dish. Enjoy your food!", TextToSpeech.QUEUE_ADD);
                }
            }
        });

    }

    private String prepareIngredientsString(List<Ingredient> ingredients) {
        String result = "";
        for (int i = 0; i < ingredients.size(); i++) {
            result += ingredients.get(i).getDescription();
            if (i < ingredients.size() - 1) {
                result+="\n";
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
        super.onDestroy();
    }

    private void speak(String words, int action) {
        tts.speak(words, action, null, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            if (tts.isLanguageAvailable(Locale.UK) == TextToSpeech.LANG_AVAILABLE)
                tts.setLanguage(Locale.UK);
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
}
