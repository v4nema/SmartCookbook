package com.reply.smartcookbook;

import android.os.AsyncTask;

import com.example.sebastianszczepaniak.cookbookspeak.models.ApplicationState;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public abstract class RestTask<I,T>  extends AsyncTask<I, Void, T> {
    private Callback<T> callback;

    public RestTask(Callback<T> callback) {
        if (callback != null)
            this.callback = callback;
        else {
            this.callback = new Callback<T>() {
                @Override
                public void callback(T value) {}
            };
        }
    }

    protected String getDomain() {
        return ApplicationState.getInstance().getServerAddress();
    }

    protected String getLangDomain() {
        ApplicationState state = ApplicationState.getInstance();
        return state.getServerAddress()+"/"+state.getLanguage();
    }

    protected static String convertStreamToString(InputStream is)
    {
        Reader reader = new InputStreamReader(is);
        StringBuilder sb = new StringBuilder();
        try
        {
            char[] buffer = new char[4096];
            int n;
            while ((n = reader.read(buffer)) > 0) {
                sb.append(buffer, 0, n);
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
