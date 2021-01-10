package com.example.diabetes.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.diabetes.R;
import com.example.diabetes.adapter.Main_Adapter;
import com.example.diabetes.fragment.*;
import com.example.diabetes.fragment.FragmentHome;
import com.example.diabetes.fragment.FragmentStep;
import com.example.diabetes.receiver.NotificationReceiver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.result.ListSubscriptionsResult;
import com.google.android.material.tabs.TabLayout;

import static com.example.diabetes.app.App.CHANNEL_ID;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "nnn";
    private NotificationManagerCompat notificationManager;
    private ViewPager mViewpager;
    private TabLayout mTabLayout;
    public static GoogleApiClient googlefit;
    private ResultCallback<Status> mSubscribeResultCallback;
    private ResultCallback<Status> mCancelSubscriptionResultCallback;
    private ResultCallback<ListSubscriptionsResult> mListSubscriptionsResultCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Main_Adapter adapter = new Main_Adapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentHome());
        adapter.addFragment(new FragmentStep());
        adapter.addFragment(new FragmentFood());
        adapter.addFragment(new FragmentAcount());
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);//
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_date);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_step);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_food);
        mTabLayout.getTabAt(3).setIcon(R.drawable.ic_user_confirm);
        showNotification();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
        initCallbacks();
    }

    private void showSubscriptions() {
        Fitness.RecordingApi.listSubscriptions(googlefit)
                .setResultCallback(mListSubscriptionsResultCallback);
    }

    private void init() {
        mViewpager = findViewById(R.id.viewPagerMain);
        mTabLayout = findViewById(R.id.tabLayoutMain);
        googlefit = new GoogleApiClient.Builder(this)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 1, this)
                .build();
    }

    private void initCallbacks() {
        mSubscribeResultCallback = new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    if (status.getStatusCode() == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                        Log.e("RecordingAPI", "Already subscribed to the Recording API");
                    } else {
                        Log.e("RecordingAPI", "Subscribed to the Recording API");
                    }
                }
            }
        };

        mCancelSubscriptionResultCallback = new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    Log.e("RecordingAPI", "Canceled subscriptions!");
                } else {
                    // Subscription not removed
                    Log.e("RecordingAPI", "Failed to cancel subscriptions");
                }
            }
        };

        mListSubscriptionsResultCallback = new ResultCallback<ListSubscriptionsResult>() {
            @Override
            public void onResult(@NonNull ListSubscriptionsResult listSubscriptionsResult) {
                for (Subscription subscription : listSubscriptionsResult.getSubscriptions()) {
                    DataType dataType = subscription.getDataType();
                    Log.e("RecordingAPI", dataType.getName());
                    for (Field field : dataType.getFields()) {
                        Log.e("RecordingAPI", field.toString());
                    }
                }
            }
        };
    }

    public void showNotification() {
        notificationManager = NotificationManagerCompat.from(this);
        RemoteViews collapsedView = new RemoteViews(getPackageName(),
                R.layout.notification_collapsed);
        Intent clickIntent = new Intent(MainActivity.this, NotificationReceiver.class);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this,
                0, clickIntent, 0);
        collapsedView.setTextViewText(R.id.text_view_collapsed_1, "Hello Khach!");
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setCustomContentView(collapsedView)
                    .setSound(null)
                    .build();
        }

        notificationManager.notify(1, notification);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationManager.cancel(1);
            }
        }, 3000);
        showSubscriptions();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "onConnected hOMe ");
        Fitness.RecordingApi.subscribe(googlefit, DataType.TYPE_STEP_COUNT_DELTA)
                .setResultCallback(mSubscribeResultCallback);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: "+ connectionResult.getErrorMessage());
    }
}