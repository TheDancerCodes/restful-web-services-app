package com.thedancercodes.android.restful.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.thedancercodes.android.restful.model.DataItem;
import com.thedancercodes.android.restful.parsers.MyXMLParser;
import com.thedancercodes.android.restful.utils.HttpHelper;
import java.io.IOException;

public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";
    public static final String MY_SERVICE_EXCEPTION = "myServiceException";

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
            response = HttpHelper.downloadUrl(uri.toString(), "nadias", "NadiasPassword");
        } catch (IOException e) {
            e.printStackTrace();

            /*
              Allow app to send exception information from the Intent Service
              back to the Visual Layer
             */

            // Get the message from the exception & package it in an intent
            Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
            messageIntent.putExtra(MY_SERVICE_EXCEPTION, e.getMessage());

            // Send exception message back to the rest of the application as a broadcast message.
            LocalBroadcastManager manager =
                    LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
            return;
        }

        // Instatiate Gson
//        Gson gson = new Gson();

        // Use Gson to transform the raw string (response) into an array of strongly typed objects
//        DataItem[] dataItems = gson.fromJson(response, DataItem[].class);

        // Get DataItem array from the Parser
        DataItem[] dataItems = MyXMLParser.parseFeed(response);

        // Use an intent object to pass data back through a Message
        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);

        // Pass data back by adding it to the intent as an Extra
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);

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
