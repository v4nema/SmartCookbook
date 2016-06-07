package com.reply.smartcookbook;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import eu.reply.smartcookbook.recipe.Ingredient;
import eu.reply.smartcookbook.recipe.Recipe;

public class JsonRecipe {

    public static List<Recipe> readCollection(String jsonString) {
        try {
            JSONArray json = new JSONObject(jsonString).getJSONArray("collection");
            ArrayList<Recipe> recipes = new ArrayList<>();
            for(int i=0; i<json.length(); i++)
                recipes.add(read(json.getJSONObject(i)));
            return recipes;
        }catch(JSONException e) {
            Log.e("JsonRecipe", "error reading recipe list", e);
            return null;
        }
    }

    public static Recipe read(String json) {
        try {
            return read(new JSONObject(json));
        }catch(JSONException e) {
            Log.e("JsonRecipe", "error reading recipe", e);
            return null;
        }
    }

    public static Recipe read(JSONObject jrep) throws JSONException {
        Recipe recipe = new Recipe(jrep.getString("source"),jrep.getString("id"));
        recipe.setName(jrep.getString("name"));

        JSONArray jings = jrep.getJSONArray("ingredients");
        ArrayList<Ingredient> ings = new ArrayList<>();
        for(int j=0; j<jings.length(); j++) {
            ings.add(new Ingredient(jings.getJSONObject(j).getString("fullDesc")));
        }
        recipe.setIngredients(ings);

        if (jrep.has("steps")) {
            JSONArray jsteps = jrep.getJSONArray("steps");
            ArrayList<String> steps = new ArrayList<>();
            for(int j=0; j<jsteps.length(); j++) {
                steps.add(jsteps.getString(j));
            }
            recipe.setSteps(steps);
        }
        return recipe;
    }
}
