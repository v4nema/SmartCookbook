package com.example.sebastianszczepaniak.cookbookspeak.models;

import com.allrecipes.Recipe;

import java.util.ArrayList;

/**
 * Created by sebastianszczepaniak on 14/05/2016.
 */
public class ApplicationState {
    private static ArrayList<String> ingredients = new ArrayList<>();
    private static ArrayList<Recipe> recipes = new ArrayList<>();
    private Recipe selectedRecipe;

    private static ApplicationState ourInstance = new ApplicationState();

    public static ApplicationState getInstance() {
        return ourInstance;
    }

    private static void checkThePantry(){

        ingredients.clear();

        ingredients.add("milk");
        ingredients.add("sausages");
        ingredients.add("cheese");
        ingredients.add("beef");
        ingredients.add("carrots");
        ingredients.add("pasta");
        ingredients.add("eggs");
    }

    public static ArrayList<String> getIngredients(){

        checkThePantry();
        return ingredients;
    }

    private static void createRecipes(){
        recipes.clear();

        final Recipe recipe1 = new Recipe("");
        recipe1.setName("Macaroni cheese");

        final Recipe recipe2 = new Recipe("");
        recipe2.setName("Spaghetti");

        final Recipe recipe3 = new Recipe("");
        recipe3.setName("Lasagne");

        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);
    }

    public static ArrayList<Recipe> getRecipes(){
        createRecipes();
        return recipes;
    }

    private ApplicationState() {

    }
}
