package com.example.diabetes.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.diabetes.R;
import com.example.diabetes.SessionManager;
import com.example.diabetes.activity.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DailyTotalResult;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class StepNotifi extends JobService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "nnn";
    private GoogleApiClient mGoogleApiClient;
    private NotificationManager mNotificationManager;
    MyAsync myAsync;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: ");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob: ");
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "onConnected ");
        myAsync.execute();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");
    }

    private int getStepToday(GoogleApiClient googleApiClient) {
        int i = 0;
        DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(googleApiClient, DataType.TYPE_STEP_COUNT_DELTA).await(1, TimeUnit.MINUTES);
        if (result.getTotal().getDataPoints().isEmpty()) {
            Log.d(TAG, "showDataSet: null");
        } else {
            for (DataPoint dp : result.getTotal().getDataPoints()) {
//                Log.e("History", "Data point:");
//                Log.e("History", "\tType: " + dp.getDataType().getName());
//                Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//                Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {
                    Log.e("nnn", "\tField: " + field.getName() +
                            " Value: " + dp.getValue(field));
                    i = Integer.parseInt(String.valueOf(dp.getValue(field)));
                }
            }
        }
        return i;
    }

//    private class ViewTodaysAndMonthStepCountTask extends AsyncTask<Integer, Void, Void> {
//        @Override
//        protected Void doInBackground(Integer... integers) {
//            getStepToday(mGoogleApiClient);
//            return null;
//        }
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .build();
        mGoogleApiClient.connect();
        myAsync = new MyAsync();

//        StepsAsync async = new StepsAsync();

//        async.execute(mGoogleApiClient);
    }

    private class MyAsync extends AsyncTask<GoogleApiClient, Void, Integer> {

        @Override
        protected Integer doInBackground(GoogleApiClient... googleApiClients) {
            int a = getStepToday(mGoogleApiClient);
            return a;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Calendar calendar = Calendar.getInstance();

            if (integer >0) {
                if (calendar.get(Calendar.HOUR_OF_DAY)>21 && calendar.get(Calendar.HOUR_OF_DAY)< 8){
                    notification(integer);
                    setTimeNotifi();
                    Log.d(TAG, "onPostExecute: if");
                } else {
                    if (calendar.get(Calendar.HOUR_OF_DAY)>8){
                        notification(integer);
                        Log.d(TAG, "onPostExecute: else if");
                        setTimeNotifi();
                    }else {
                        Log.d(TAG, "onPostExecute: else 2");
                        setTimeNotifi();
                    }
                }
            } else {
                Log.d(TAG, "onPostExecute: else");
                setTimeNotifi();
            }
        }
    }

    private void notification(int step) {
        String notifi = "";
        if (step < 100) {
            notifi = "Bạn đã đi được " + step+" bước cố gắng lên nào";
        } else if (step >= 100 && step < 1000){
            notifi = "Giỏi quá! bạn đi được "+step+" bước rồi đó";
        } else {
            SessionManager.getInstance().setKeySaveCheck(true);
            notifi = "Quá xuất sắc "+step+" bước rồi";
        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "notify_001");
        Intent ii = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_step);
//            mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText(notifi);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());

//        long when = System.currentTimeMillis() + 10000L;
//        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
//        intent.putExtra("myAction", "notifi");
//        PendingIntent pe = PendingIntent.getBroadcast(getApplicationContext(), 5555, intent, 0);
//        am.set(AlarmManager.RTC_WAKEUP, when, pe);
    }

    private void setTimeNotifi(){
        long when = System.currentTimeMillis() + 10000L;
//        Calendar cldset = Calendar.getInstance();
//        cldset.set(Calendar.HOUR_OF_DAY,5);
//        cldset.set(Calendar.MINUTE,01);
//        cldset.set(Calendar.MILLISECOND,01);
//        when = cldset.getTimeInMillis();
        when = System.currentTimeMillis() + 108000000L;
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.putExtra("myAction", "notifi");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 5555, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
