package com.reply.smartcookbook;

import android.util.Log;

import com.allrecipes.Recipe;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsTask extends RestTask<Recipe> {

    public RecipeDetailsTask(Callback<Recipe> callback) {
        super(callback);
    }

    public RecipeDetailsTask(String domain, Callback<Recipe> callback) {
        super(domain,callback);
    }

    @Override
    protected Recipe doInBackground(String... params) {
        HttpPost request = new HttpPost(domain+"/SmartCookBook/rest/ws/service2/");
        try
        {
            ArrayList<NameValuePair> valpairs = new ArrayList<>();
            valpairs.add(new BasicNameValuePair("url",params[0]));
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
