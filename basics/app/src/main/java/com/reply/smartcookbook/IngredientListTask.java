package com.reply.smartcookbook;

import com.example.sebastianszczepaniak.cookbookspeak.models.ApplicationState;
import com.example.sebastianszczepaniak.cookbookspeak.models.IngredientList;

import org.apache.http.Header;
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

public class IngredientListTask extends RestTask<String,IngredientList> {

    public IngredientListTask(Callback<IngredientList> callback) {
        super(callback);
    }

    @Override
    protected IngredientList doInBackground(String... params) {
        HttpGet request = new HttpGet(getLangDomain()+"/ingredients");
        try
        {
            if (params.length > 0 && params[0] != null) {
                request.setHeader("If-Modified-Since", params[0]);
            }
            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(request);
            if (httpResponse.getStatusLine().getStatusCode() == 304)
                return null; //not modified

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

                Header lastMod = httpResponse.getFirstHeader("Last-Modified");
                return new IngredientList(ingredients,
                        lastMod == null ? null : lastMod.getValue());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
