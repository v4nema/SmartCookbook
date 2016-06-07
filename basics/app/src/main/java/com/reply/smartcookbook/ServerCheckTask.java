package com.reply.smartcookbook;

import com.example.sebastianszczepaniak.cookbookspeak.models.ApplicationState;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ServerCheckTask extends RestTask<Void,Void> {

    public ServerCheckTask(Callback<Void> callback) {
        super(callback);
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpGet request = new HttpGet(getDomain()+"/server");
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(request);
            int responseCode = httpResponse.getStatusLine().getStatusCode();

            if (responseCode != 304) {
                HttpEntity entity = httpResponse.getEntity();

                if (entity != null)
                {
                    InputStream instream = entity.getContent();
                    String response = convertStreamToString(instream);
                    instream.close();
                    ApplicationState.getInstance().setServerAddress(response);
                }
            }
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
