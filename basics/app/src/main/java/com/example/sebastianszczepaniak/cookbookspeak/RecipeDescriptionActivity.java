package com.example.sebastianszczepaniak.cookbookspeak;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.allrecipes.Recipe;

import java.util.Locale;

public class RecipeDescriptionActivity extends Activity implements TextToSpeech.OnInitListener {

    private static final int MY_DATA_CHECK_CODE = 0;
    private static final String SELECTED_RECIPE = "selected_recipe";
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_description);

        Button speakButton = (Button) findViewById(R.id.speak);
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText enteredText = (EditText) findViewById(R.id.enter);
                String words = enteredText.getText().toString();
                speak(words);
            }
        });

        final Recipe selectedRecipe = (Recipe) getIntent().getExtras().get(SELECTED_RECIPE);

        prepareViewForRecipe(selectedRecipe);
        //todo create a button to play/next method
        //todo before speaking the current method, highlight the correct text view

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    private void prepareViewForRecipe(Recipe selectedRecipe) {
        //todo replace title with recipe title

        //todo replace ingredients with ingredients list

        //todo create a list of steps for recipe and build a list of text views
        //todo put the list of steps into recipe_steps_container

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
