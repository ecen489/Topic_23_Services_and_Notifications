package com.example.t23_services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private NotificationCompat.Builder notification_builder; // This will be used to build your notification
    private NotificationManagerCompat notification_manager;// This will be used to display the notification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ////////////////////
        // The below code sets up an intent filter to process broadcast intents from teh service

        IntentFilter filter = new IntentFilter();
        filter.addAction("playerStopped");
        registerReceiver(new MyReceiver(), filter);

        ////////////////////
        //  The below code sets up a notification manager and builder

        NotificationManager notification_manager = (NotificationManager) this
                .getSystemService(this.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//This portion of the code handles newer APIs
            String chanel_id = "3000";
            CharSequence name = "Channel Name";
            String description = "Chanel Description";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            notification_manager.createNotificationChannel(mChannel);
            notification_builder = new NotificationCompat.Builder(this, chanel_id);
        } else {
            notification_builder = new NotificationCompat.Builder(this); //this code handles older APIs
        }
        ////////////////////
    }

    /////////Start Service
    public void buttonStart(View view) {
        startService(new Intent(this, MyService.class));
    }

    /////////Stop Service

    public void buttonStop(View view) {
        stopService(new Intent(this, MyService.class));
    }


    ////////Start new activity (this shows that the service is till active even when a new activity is started)

    public void nextPage(View view) {
        Intent intent=new Intent(this,NextActivity.class);
        startActivity(intent);
    }

    /////////Broadcast receiver shows the content of the broadcast intent using a Toast.  It also copies the text into a notifcation and displays it.
    private class MyReceiver extends BroadcastReceiver{

        //Get the content from the broadcast intent.  Show it in a Toast
        @Override
        public void onReceive(Context context, Intent intent) {
            String mesg = intent.getStringExtra("key1");
            Toast.makeText(MainActivity.this, mesg, Toast.LENGTH_SHORT).show();

            //Build a notification with the same information, and show it.
            notification_builder.setSmallIcon(R.drawable.baseline_notification)
                    .setContentTitle("The Service Says")
                    .setContentText(mesg)
                    .setAutoCancel(true);

            Notification notification = notification_builder.build();
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1, notification);


        }
    }



}
