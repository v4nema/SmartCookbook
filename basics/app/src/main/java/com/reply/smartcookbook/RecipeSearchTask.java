package com.reply.smartcookbook;

import android.os.AsyncTask;
import android.util.Log;

import com.allrecipes.Ingredient;
import com.allrecipes.Recipe;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RecipeSearchTask extends RestTask<List<Recipe>> {

    public RecipeSearchTask(Callback<List<Recipe>> callback) {
        super(callback);
    }

    public RecipeSearchTask(String domain,Callback<List<Recipe>> callback) {
        super(domain,callback);
    }

    @Override
    protected List<Recipe> doInBackground(String... params) {
        HttpPost request = new HttpPost(domain+"/SmartCookBook/rest/ws/service1/");
        try
        {
            ArrayList<NameValuePair> valpairs = new ArrayList<>();
            for (String ing : params) {
                if (!ing.isEmpty())
                    valpairs.add(new BasicNameValuePair("ingredient",ing));
            }
            request.setEntity(new UrlEncodedFormEntity(valpairs));
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
                return JsonRecipe.readCollection(response);
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
