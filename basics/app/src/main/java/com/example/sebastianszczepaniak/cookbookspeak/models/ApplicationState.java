package com.example.sebastianszczepaniak.cookbookspeak.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.reply.smartcookbook.recipe.Ingredient;
import eu.reply.smartcookbook.recipe.Recipe;

/**
 * Created by sebastianszczepaniak on 14/05/2016.
 */
public class ApplicationState {

    private List<String> ingredients;
    private List<Recipe> recipes;
    private Recipe selectedRecipe;
    private String serverAddress = "https://smart-cookbook.herokuapp.com";
    private String lang = "";
    private boolean serverCheck = false;

    private static ApplicationState ourInstance = new ApplicationState();

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        if (serverAddress != null)
            this.serverAddress = serverAddress;
    }

    public boolean isServerChecked() {
        return serverCheck;
    }

    public void setServerChecked() {
        serverCheck = true;
    }

    public static ApplicationState getInstance() {
        return ourInstance;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public String getLanguage() {
        return lang;
    }

    public void setLanguage(String lang) {
        this.lang = lang;
    }

    private void checkThePantry() {

        if(ingredients == null || !ingredients.isEmpty()) return;

        if ("it".equals(getLanguage())) {
            ingredients.add("latte");
            ingredients.add("salsicce");
            ingredients.add("formaggio");
            ingredients.add("manzo");
            ingredients.add("carote");
            ingredients.add("pasta");
            ingredients.add("uova");
        } else {
            ingredients.add("milk");
            ingredients.add("sausages");
            ingredients.add("cheese");
            ingredients.add("beef");
            ingredients.add("carrots");
            ingredients.add("pasta");
            ingredients.add("eggs");
        }
    }

    public List<String> getIngredients() {

        checkThePantry();
        return ingredients;
    }

    private void createRecipes() {
        if(recipes == null || !recipes.isEmpty()) return;
//        recipes.clear();

        final String step1 = "1. Cook the macaroni in a large saucepan of boiling salted water for 8-10 minutes; drain well and set aside.";
        final String step2 = "2. Melt the butter over a medium heat in a saucepan slightly larger than that used for the macaroni. Add the flour and stir to form a roux, cooking for a few minutes.";
        final String step3 = "3. Gradually whisk in the milk, a little at a time. Cook for 10-15 minutes to a thickened and smooth sauce.";

        final List<String> steps = new ArrayList<>();
        steps.add(step1);
        steps.add(step2);
        steps.add(step3);

        final Ingredient ingredient1 = new Ingredient("250g maccaroni");
        final Ingredient ingredient2 = new Ingredient("40g butter");
        final Ingredient ingredient3 = new Ingredient("40g flour");
        final Ingredient ingredient4 = new Ingredient("600ml milk");
        final Ingredient ingredient5 = new Ingredient("250g grated cheese");

        final List<Ingredient> ingredients = new ArrayList<>();
        ingredients.addAll(Arrays.asList(new Ingredient[]{ingredient1, ingredient2, ingredient3, ingredient4, ingredient5}));

        final Recipe recipe1 = new Recipe("");
        recipe1.setName("Macaroni cheese");
        recipe1.setSteps(steps);
        recipe1.setIngredients(ingredients);

        final Recipe recipe2 = new Recipe("");
        recipe2.setName("Spaghetti");
        recipe2.setSteps(steps);
        recipe2.setIngredients(ingredients);

        final Recipe recipe3 = new Recipe("");
        recipe3.setName("Lasagne");
        recipe3.setSteps(steps);
        recipe3.setIngredients(ingredients);

        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);
    }

    public List<Recipe> getRecipes() {
        createRecipes();
        return recipes;
    }

    public Recipe getSelectedRecipe() {
        return selectedRecipe;
    }

    public void setSelectedRecipe(Recipe selectedRecipe) {
        this.selectedRecipe = selectedRecipe;
    }

    private ApplicationState() {
        ingredients = new ArrayList<>();
        recipes = new ArrayList<>();
    }
}
