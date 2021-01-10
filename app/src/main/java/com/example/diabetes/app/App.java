package com.example.diabetes.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.diabetes.SessionManager;

public class App extends Application {
    public static final String CHANNEL_ID = "exampleChannel";
    @Override
    public void onCreate() {
        super.onCreate();
        SessionManager.getInstance().init(this);
//        createNotificationChannel();
    }
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "Example Channel",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//    }
}