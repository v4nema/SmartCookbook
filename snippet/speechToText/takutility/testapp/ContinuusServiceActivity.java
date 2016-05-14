package takutility.testapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class ContinuusServiceActivity extends Activity //implements         RecognitionListener
{
    private static final String TAG = "SpeechRecogn";

    private static VoiceCommandService voiceCommandService;
    private static Context activityContext;
    private int mBindFlag;
    private Messenger mServiceMessenger;

    public static void init(Context context)
    {
        voiceCommandService = new VoiceCommandService();
        activityContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init(getApplicationContext());

        Intent service = new Intent(activityContext, VoiceCommandService.class);
        activityContext.startService(service);
        mBindFlag = Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ? 0 : Context.BIND_ABOVE_CLIENT;
        startContinuousListening();
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        bindService(new Intent(this, VoiceCommandService.class), mServiceConnection, mBindFlag);
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        if (mServiceMessenger != null)
        {
            unbindService(mServiceConnection);
            mServiceMessenger = null;
        }
    }
    private final ServiceConnection mServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            if (true) {Log.d(TAG, "onServiceConnected");} //$NON-NLS-1$

            mServiceMessenger = new Messenger(service);
            Message msg = new Message();
            msg.what = VoiceCommandService.MSG_RECOGNIZER_START_LISTENING;

            try
            {
                mServiceMessenger.send(msg);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            if (true) {
                Log.d(TAG, "onServiceDisconnected");} //$NON-NLS-1$
            mServiceMessenger = null;
        }

    }; // mServiceConnection

    public static void startContinuousListening()
    {
        Message msg = new Message();
        msg.what = VoiceCommandService.MSG_RECOGNIZER_START_LISTENING;

        try
        {
            voiceCommandService.mServerMessenger.send(msg);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }

    }
}
