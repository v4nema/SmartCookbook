package com.example.sebastianszczepaniak.cookbookspeak.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sebastianszczepaniak.cookbookspeak.R;
import com.example.sebastianszczepaniak.cookbookspeak.RecipeDescriptionActivity;
import com.example.sebastianszczepaniak.cookbookspeak.RecipiesActivity;
import com.example.sebastianszczepaniak.cookbookspeak.models.ApplicationState;
import com.reply.smartcookbook.Callback;
import com.reply.smartcookbook.RecipeDetailsTask;
import com.reply.smartcookbook.RecipeSearchTask;

import java.util.List;

import eu.reply.smartcookbook.recipe.Recipe;

/**
 * Created by sebastianszczepaniak on 14/05/2016.
 */
public class RecipeItemAdapter extends BaseAdapter {

    private static final String SELECTED_RECIPE = "selected_recipe";

    private final List<Recipe> recipeList;
    private Context context;

    public RecipeItemAdapter(final Context context, final List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return recipeList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View recipeItemSingle;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        recipeItemSingle = inflater.inflate(R.layout.recipe_list_item, null);

        TextView recipeTitle = (TextView) recipeItemSingle.findViewById(R.id.recipe_item_title);
        final Recipe currentRecipe = recipeList.get(position);

        recipeTitle.setText(currentRecipe.getName());

        recipeItemSingle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                RecipeDetailsTask task = new RecipeDetailsTask(new Callback<Recipe>() {
                    @Override
                    public void callback(Recipe value)
                    {
                        ApplicationState.getInstance().setSelectedRecipe(value);
                        Intent i = new Intent(context.getApplicationContext(), RecipeDescriptionActivity.class);
                        context.startActivity(i);
                    }
                });
                task.execute(currentRecipe);
            }
        });

        return recipeItemSingle;
    }
}
