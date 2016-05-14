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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeDescriptionActivity extends Activity implements TextToSpeech.OnInitListener {

    private static final int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech tts;

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
        for (String step : selectedRecipe.getSteps()) {
            final TextView stepTextView = new TextView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int margin_bottom_5 = (int) getResources().getDimension(R.dimen.margin_bottom_5);
            params.setMargins(0, 0, 0, margin_bottom_5);
            stepTextView.setLayoutParams(params);
            stepTextView.setText(step);
            stepTextView.setTextColor(Color.parseColor("#000000"));
            methodStepsContainer.addView(stepTextView);
            stepTextViews.add(stepTextView);
        }
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

    private void speak(String words) {
        tts.speak(words, TextToSpeech.QUEUE_FLUSH, null, null);
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
