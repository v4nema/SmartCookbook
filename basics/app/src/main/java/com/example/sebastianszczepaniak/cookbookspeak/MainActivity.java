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
import com.example.sebastianszczepaniak.cookbookspeak.models.ApplicationState;
import com.reply.smartcookbook.Callback;
import com.reply.smartcookbook.RecipeSearchTask;

import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText ingredientsBox = (EditText) findViewById(R.id.ingredientsBox);
        ingredientsBox.setText("");

        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecipeSearchTask task = new RecipeSearchTask(new Callback<List<Recipe>>() {
                    @Override
                    public void callback(List<Recipe> value)
                    {
                        ApplicationState.getInstance().setRecipes(value);
                        Intent intent = new Intent(MainActivity.this, RecipiesActivity.class);
                        startActivity(intent);
                    }
                });
                task.execute(ingredientsBox.getText().toString().split(" "));
            }
        });


        findViewById(R.id.pantryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder stringBuilder = new StringBuilder();
                for (String s : ApplicationState.getInstance().getIngredients()) {
                    stringBuilder.append(s).append(" ");
                }

                ingredientsBox.setText(stringBuilder.toString());
            }
        });


        final EditText serverAddress = (EditText) findViewById(R.id.serverAddressText);

        findViewById(R.id.serverAddressButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = serverAddress.getText().toString();
                ApplicationState.getInstance().setServerAddress(address);
            }
        });
    }

}
