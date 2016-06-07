package com.reply.smartcookbook;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

import eu.reply.smartcookbook.recipe.Recipe;

public class RecipeDetailsTask extends RestTask<Recipe,Recipe> {

    public RecipeDetailsTask(Callback<Recipe> callback) {
        super(callback);
    }

    @Override
    protected Recipe doInBackground(Recipe... params) {
        HttpGet request = new HttpGet(getDomain()+"/get/"+params[0].getSource()+"/"+params[0].getId());
        try
        {
            HttpClient client = new DefaultHttpClient();

            HttpResponse httpResponse = client.execute(request);
            //responseCode = httpResponse.getStatusLine().getStatusCode();
            //message = httpResponse.getStatusLine().getReasonPhrase();
            HttpEntity entity = httpResponse.getEntity();

            if (entity != null)
            {
                InputStream instream = entity.getContent();
                String response = convertStreamToString(instream);
                instream.close();
                return JsonRecipe.read(response);
            }
            return null;
        }
        catch (Exception e)
        {
            Log.e("REST", "search error",e);
            return null;
        }
    }
}
