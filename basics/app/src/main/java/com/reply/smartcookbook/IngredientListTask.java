package com.reply.smartcookbook;

import com.example.sebastianszczepaniak.cookbookspeak.models.ApplicationState;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class IngredientListTask extends RestTask<Void,Set<String>> {

    public IngredientListTask(Callback<Set<String>> callback) {
        super(callback);
    }

    @Override
    protected Set<String> doInBackground(Void... params) {
        HttpGet request = new HttpGet(getLangDomain()+"/ingredients");
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(request);
            HttpEntity entity = httpResponse.getEntity();

            if (entity != null)
            {
                InputStream instream = entity.getContent();
                String response = convertStreamToString(instream);
                instream.close();

                Set<String> ingredients = new HashSet<>();
                JSONArray arr = new JSONArray(response);
                for (int i=0; i<arr.length(); i++) {
                    ingredients.add(arr.getString(i));
                }

                return ingredients;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
