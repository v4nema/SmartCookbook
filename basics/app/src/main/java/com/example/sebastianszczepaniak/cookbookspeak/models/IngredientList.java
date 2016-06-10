package com.example.sebastianszczepaniak.cookbookspeak.models;

import java.util.Set;

/**
 * Created by f.debenedictis on 09/06/2016.
 */
public class IngredientList {
    private Set<String> ingredients;
    private String lastUpdate;

    public IngredientList(Set<String> ingredients, String lastUpdate) {
        this.ingredients = ingredients;
        this.lastUpdate = lastUpdate;
    }

    public Set<String> getIngredients() {
        return ingredients;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }
}
