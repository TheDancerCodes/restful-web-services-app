package com.thedancercodes.android.restful.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.thedancercodes.android.restful.utils.HttpHelper;
import java.io.IOException;

public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Get the intent object & extract any data I need
        Uri uri = intent.getData(); // Returns the Uri object passed in from the activity

        // Display Output
        Log.i(TAG, "onHandleIntent: " + uri.toString());

        String response;

        // Instance of HttpHelper class to get a response
        try {
            response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Use an intent object to pass data back through a Message
        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);

        // Pass data back by adding it to the intent as an Extra
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, response);

        //Broadcast the message
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

}
