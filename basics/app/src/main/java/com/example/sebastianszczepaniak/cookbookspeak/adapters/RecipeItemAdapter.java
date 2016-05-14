package com.example.sebastianszczepaniak.cookbookspeak.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.allrecipes.Recipe;
import com.example.sebastianszczepaniak.cookbookspeak.R;
import com.example.sebastianszczepaniak.cookbookspeak.RecipeDescriptionActivity;

import java.util.List;

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
        final Recipe currectRecipe = recipeList.get(position);

        recipeTitle.setText(currectRecipe.getName());

        recipeItemSingle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(context.getApplicationContext(), RecipeDescriptionActivity.class);
                i.putExtra(SELECTED_RECIPE, currectRecipe);
                context.startActivity(i);
            }
        });

        return recipeItemSingle;
    }
}
