package com.example.diabetes.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diabetes.R;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.example.diabetes.activity.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class FragmentHome extends Fragment {
    private View view;
    private TextView txtFull, txtMounth, tvStepTodayHome, tvStepWeekHome;
    private int stepTodate = 0;
    private int stepMonth = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_home,container,false);
       init();
        setDataTime();
        ViewTodaysAndMonthStepCountTask task = new ViewTodaysAndMonthStepCountTask();
        task.execute();
        return view;
    }

    private void init() {
        txtFull = view.findViewById(R.id.txtFull);
        txtMounth = view.findViewById(R.id.txtMounth);
        tvStepTodayHome = view.findViewById(R.id.tvStepTodayHome);
        tvStepWeekHome = view.findViewById(R.id.tvStepWeekHome);
    }

    private void setDataTime(){
        Calendar starDate = Calendar.getInstance();
        txtFull.setText(starDate.get(Calendar.YEAR)+"/"+(starDate.get(Calendar.MONTH)+1)+"/"+starDate.get(Calendar.DAY_OF_MONTH));
        txtMounth.setText("月: "+((starDate.get(Calendar.MONTH)+1)));

        starDate.add(Calendar.MONTH, -1);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(starDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                Date date1 = date.getTime();
                txtFull.setText(date1.toString()+"");
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });
    }


    //AsyncTask
    private class ViewTodaysAndMonthStepCountTask extends AsyncTask<Integer, Void, Map<String,Integer>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Map<String,Integer> doInBackground(Integer... integers) {
            Calendar calendar = Calendar.getInstance();
            stepTodate = callgooglefit(0, 24, calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
            stepMonth = callgooglefitOne();
            Map<String,Integer> map = new HashMap<>();
            map.put("n1",stepMonth);
            map.put("n2",stepTodate);
            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Integer> map) {
            super.onPostExecute(map);
            if (!map.isEmpty()){
                tvStepTodayHome.setText("" + map.get("n2"));
                tvStepWeekHome.setText("" + map.get("n1"));
            }
        }
    }



    //gọi trong StepinDay
    private int callgooglefit(int start, int end, int date, int month, int year) {

        int i = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, date);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, start);
        long startTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, end);
        long endTime = calendar.getTimeInMillis();
        calendar.set(Calendar.MINUTE, 0);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm /MM/yyyy");
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

        DataReadResult dataReadResult = Fitness.HistoryApi.readData(MainActivity.googlefit, readRequest).await(1, TimeUnit.MINUTES);

        //Used for aggregated data
        if (dataReadResult.getBuckets().size() > 0) {
//            Log.e("History", "Number of buckets: if " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (com.google.android.gms.fitness.data.DataSet dataSet : dataSets) {
                    i = getdatastep(dataSet);
                }
            }
        }
        //Used for non-aggregated data
        else if (dataReadResult.getDataSets().size() > 0) {
//            Log.e("History", "Number of returned DataSets: else " + dataReadResult.getDataSets().size());
            for (com.google.android.gms.fitness.data.DataSet dataSet : dataReadResult.getDataSets()) {
                i = getdatastep(dataSet);
            }
        }
        return i;
    }

    //gọi trong callgooglefit
    private int getdatastep(com.google.android.gms.fitness.data.DataSet dataSet) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        int ret = 0;
        if (dataSet.getDataPoints().isEmpty()) {
            ret = 0;
        } else {
            for (DataPoint dp : dataSet.getDataPoints()) {
//                Log.d(TAG, "getdatastep: " + format.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
//                Log.d(TAG, "getdatastep: " + dp.getValue(Field.FIELD_STEPS));
//                Log.e("History", "\tType: " + dp.getDataType().getName());
                for (Field field : dp.getDataType().getFields()) {
//                    Log.e("History", "\tField: " + field.getName() +
//                            " Value: " + dp.getValue(field));
                    ret = Integer.parseInt(String.valueOf(dp.getValue(field)));
                }
            }
        }
        return ret;
    }

    // get dữ liệu theo tháng
    private int callgooglefitOne() {

        int i = 0;
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_MONTH, -1);
        long startTime = cal.getTimeInMillis();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        DataReadResult dataReadResult = Fitness.HistoryApi.readData(MainActivity.googlefit, readRequest).await(1, TimeUnit.MINUTES);

        //Used for aggregated data
        if (dataReadResult.getBuckets().size() > 0) {
//            Log.e("History", "Number of buckets: if " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<com.google.android.gms.fitness.data.DataSet> dataSets = bucket.getDataSets();
                for (com.google.android.gms.fitness.data.DataSet dataSet : dataSets) {
                    i += getdatastep(dataSet);
                }
            }
        }
        //Used for non-aggregated data
        else if (dataReadResult.getDataSets().size() > 0) {
//            Log.e("History", "Number of returned DataSets: else " + dataReadResult.getDataSets().size());
            for (com.google.android.gms.fitness.data.DataSet dataSet : dataReadResult.getDataSets()) {
                i += getdatastep(dataSet);
            }
        }
        return i;
    }

    @Override
    public void onResume() {
        super.onResume();
//        settextstepTodayandMonth();
    }
}
