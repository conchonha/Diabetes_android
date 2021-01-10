package com.example.diabetes.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.diabetes.R;
import com.example.diabetes.activity.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "nnn";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context mContext, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,50);

        if(intent.getStringExtra("myAction") != null){
            ComponentName serviceComponent = new ComponentName(mContext, StepNotifi.class);
            JobInfo.Builder builder = new JobInfo.Builder(1, serviceComponent);
            builder.setMinimumLatency(1 * 1000);
            builder.setOverrideDeadline(2 * 1000);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
            builder.setRequiresDeviceIdle(true);
            builder.setRequiresCharging(false);
            PersistableBundle bundle = new PersistableBundle();
            bundle.putString("abc","123");
            builder.setExtras(bundle);
            JobScheduler jobScheduler =
                    (JobScheduler) mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());

        }
    }
}