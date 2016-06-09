package com.example.sebastianszczepaniak.cookbookspeak;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sebastianszczepaniak.cookbookspeak.models.ApplicationState;
import com.reply.smartcookbook.AvailableLanguagesTask;
import com.reply.smartcookbook.Callback;
import com.reply.smartcookbook.RecipeSearchTask;
import com.reply.smartcookbook.ServerCheckTask;

import java.util.List;

import eu.reply.smartcookbook.recipe.Recipe;

public class MainActivity extends Activity {
    private static final String SERVER_ADDRESS = "server.address";
    private static final String SERVER_LANG = "server.language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences pref = getPreferences(MODE_PRIVATE);
        final ApplicationState state = ApplicationState.getInstance();
        state.setServerAddress(pref.getString(SERVER_ADDRESS,null));
        state.setLanguage(pref.getString(SERVER_LANG,""));

        if (!state.isServerChecked()) {
            new ServerCheckTask(new Callback<String>() {
                @Override
                public void callback(String value) {
                    state.setServerChecked();
                    final SharedPreferences.Editor editor = pref.edit();
                    if (value != null) {
                        editor.putString(SERVER_ADDRESS, value);
                        editor.commit();
                    }

                    if (state.getLanguage().isEmpty()) {
                        new AvailableLanguagesTask(new Callback<String>() {
                            @Override
                            public void callback(String lang) {
                                editor.putString(SERVER_LANG, lang);
                                editor.commit();
                            }
                        }).execute();
                    }
                }
            }).execute();
        }

        final EditText ingredientsBox = (EditText) findViewById(R.id.ingredientsBox);
        ingredientsBox.setText("");

        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecipeSearchTask task = new RecipeSearchTask(new Callback<List<Recipe>>() {
                    @Override
                    public void callback(List<Recipe> value)
                    {
                        if (value == null) {
                            Toast.makeText(MainActivity.this, R.string.error_search, Toast.LENGTH_LONG).show();
                            return;
                        }
                        state.setRecipes(value);
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
                for (String s : state.getIngredients()) {
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
                state.setServerAddress(address);
            }
        });

        findViewById(R.id.tellMeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StandardActivity.class);
                startActivity(intent);
            }
        });
    }

}
