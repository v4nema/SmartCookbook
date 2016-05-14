package com.example.sebastianszczepaniak.cookbookspeak;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.allrecipes.Recipe;
import com.example.sebastianszczepaniak.cookbookspeak.adapters.RecipeItemAdapter;
import com.example.sebastianszczepaniak.cookbookspeak.models.ApplicationState;

import java.util.ArrayList;
import java.util.List;

public class RecipiesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipies);

        final ListView recipesList = (ListView) findViewById(R.id.recipesList);
        recipesList.setAdapter(new RecipeItemAdapter(this, ApplicationState.getInstance().getRecipes()));
    }
}
