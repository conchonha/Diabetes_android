package com.example.diabetes.receiver;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
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

public class StepsAsync extends AsyncTask<GoogleApiClient,Void,Void> {


    private int getStepToday(GoogleApiClient googleApiClient) {
        int i = 0;
        DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(googleApiClient, DataType.TYPE_STEP_COUNT_DELTA).await(1, TimeUnit.MINUTES);
        if (result.getTotal().getDataPoints().isEmpty()) {
            Log.d("nnn", "showDataSet: null");
        } else {
            for (DataPoint dp : result.getTotal().getDataPoints()) {
//                Log.e("History", "Data point:");
//                Log.e("History", "\tType: " + dp.getDataType().getName());
//                Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//                Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {
                    Log.d("nnn", "callgooglefit: "+ i);
                    i = Integer.parseInt(String.valueOf(dp.getValue(field)));
                }
            }
        }
        return i;
    }

    private int callgooglefit(GoogleApiClient googleApiClient) {

        int i = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DATE);
        long startTime = calendar.getTimeInMillis();
        calendar.get(Calendar.DATE-1);
        long endTime = calendar.getTimeInMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
//        Log.d(TAG, "callgooglefit: "+simpleDateFormat.format(calendar.getTime()));
//        Log.d(TAG, "callgooglefit: "+calendar.get(Calendar.DATE));
        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
//        Log.e("History", "Range Start: " + dateFormat.format(startTime));
//        Log.e("History", "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        DataReadResult dataReadResult = Fitness.HistoryApi.readData(googleApiClient, readRequest).await(1, TimeUnit.MINUTES);

        //Used for aggregated data
        if (dataReadResult.getBuckets().size() > 0) {
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (com.google.android.gms.fitness.data.DataSet dataSet : dataSets) {
                    if (dataSet.getDataPoints().isEmpty()) {
                    } else {
                        for (DataPoint dp : dataSet.getDataPoints()) {
                            for (Field field : dp.getDataType().getFields()) {
                                i = Integer.parseInt(String.valueOf(dp.getValue(field))) + 0;
                                Log.d("nnn", "callgooglefit: "+ i);
                            }
                        }
                    }
                }
            }
        }
        //Used for non-aggregated data
        else if (dataReadResult.getDataSets().size() > 0) {
            for (com.google.android.gms.fitness.data.DataSet dataSet : dataReadResult.getDataSets()) {
                if (dataSet.getDataPoints().isEmpty()) {
                } else {
                    for (DataPoint dp : dataSet.getDataPoints()) {
                        for (Field field : dp.getDataType().getFields()) {
                            i = Integer.parseInt(String.valueOf(dp.getValue(field))) + 0;
                            Log.d("nnn", "callgooglefit: "+ i);
                        }
                    }
                }
            }
        }
        return i;
    }

    @Override
    protected Void doInBackground(GoogleApiClient... googleApiClients) {
        getStepToday(googleApiClients[0]);
//        callgooglefit(googleApiClients[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
