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
import java.util.Locale;

public class AvailableLanguagesTask extends RestTask<String> {

    public AvailableLanguagesTask(Callback<String> callback) {
        super(callback);
    }

    @Override
    protected String doInBackground(String... params) {
        HttpGet request = new HttpGet(getDomain()+"/languages");
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

                JSONObject json = new JSONObject(response);
                String locale = Locale.getDefault().getLanguage();
                JSONArray arr = json.getJSONArray("list");
                String lang = json.getString("default");
                for (int i=0; i<arr.length(); i++) {
                    if (locale.equals(arr.getString(i))) {
                        lang = arr.getString(i);
                        break;
                    }
                }

                ApplicationState.getInstance().setLanguage(lang);
                return lang;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ApplicationState.getInstance().getLanguage();
    }
}
