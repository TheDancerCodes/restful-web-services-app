package com.thedancercodes.android.restful;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import android.widget.Toast;
import com.thedancercodes.android.restful.services.MyService;
import com.thedancercodes.android.restful.utils.NetworkHelper;

public class MainActivity extends AppCompatActivity {

    private static final String JSON_URL =
            "http://560057.youcanlearnit.net/services/json/itemsfeed.php";

    private boolean networkOk;
    TextView output;

    // Instantiate the Broadcast Receiver
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Receive the intent object that was passed when the message is broadcast.
            String message =
                    intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);

            // Output Results
            output.append(message + "\n");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);

        // Register to listen for the message using an intent filter that looks for
        // the message identifier string.
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));

        networkOk = NetworkHelper.hasNetworkAccess(this);
        output.append("Network ok: " + networkOk);
    }

    // Unregister the listener object whenever the activity is closing down
    // to make sure you don’t have a resource leak
    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    public void runClickHandler(View view) {

        // Conditional to check for network
        if (networkOk) {

            Intent intent = new Intent(this, MyService.class);

            // Pass in the data as a URI Object
            intent.setData(Uri.parse(JSON_URL));
            startService(intent);
        } else {
            Toast.makeText(this, "Network Not Available", Toast.LENGTH_SHORT).show();
        }


    }

    public void clearClickHandler(View view) {
        output.setText("");
    }

}
