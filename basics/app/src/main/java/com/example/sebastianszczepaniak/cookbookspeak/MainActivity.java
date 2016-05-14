package com.example.sebastianszczepaniak.cookbookspeak;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sebastianszczepaniak.cookbookspeak.models.ApplicationState;

import java.util.Locale;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecipiesActivity.class);
                intent.putExtra("recipes", ApplicationState.getRecipes());
                startActivity(intent);
            }
        });

        final EditText ingredientsBox = (EditText) findViewById(R.id.ingredientsBox);
        ingredientsBox.setText("");

        findViewById(R.id.pantryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String ingredients = ApplicationState.getIngredients().toString();

                ingredientsBox.setText(ingredients);
            }
        });

//        final Intent i = new Intent(getApplicationContext(), RecipeDescriptionActivity.class);
//        startActivity(i);
    }

}
