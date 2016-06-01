package com.reply.smartcookbook;

import android.os.AsyncTask;

import com.allrecipes.Recipe;
import com.example.sebastianszczepaniak.cookbookspeak.models.ApplicationState;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public abstract class RestTask<T>  extends AsyncTask<String, Void, T> {
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
