package com.reply.smartcookbook;

import android.os.AsyncTask;

import com.allrecipes.Recipe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public abstract class RestTask<T>  extends AsyncTask<String, Void, T> {
    protected String domain = "http://172.30.3.185:8080";
    private Callback<T> callback;

    public RestTask(Callback<T> callback) {
        this.callback = callback;
    }

    public RestTask(String domain,Callback<T> callback) {
        this(callback);
        this.domain = domain;
    }

    protected static String convertStreamToString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        return sb.toString();
    }

    protected void onPostExecute(T result) {
        callback.callback(result);
    }

}
