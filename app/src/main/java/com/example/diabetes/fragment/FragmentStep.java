package com.example.diabetes.fragment;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.diabetes.R;
import com.example.diabetes.SessionManager;
import com.example.diabetes.activity.MainActivity;
import com.example.diabetes.receiver.NotificationReceiver;
import com.example.diabetes.receiver.StepNotifi;
import com.example.diabetes.util.dialog.DialogShow;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class FragmentStep extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "History";
    private View view;
    private CombinedChart mChart;
    private TextView txtFull, tvSteps, tvStepToday, tvTimeToday, tvStepMonth, tvTimeTodayDistance;
    //    private GoogleApiClient mGoogleApiClient;
    int data[];
    private BarChart barChart;
    private int sum = 0;
    private int stepTodate = 0;
    private int stepMonth = 0;
    public AsyncTask task, one;
    Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_step, container, false);
        init();
        setDataTime();
        Calendar calendar = Calendar.getInstance();
        Calendar calendarCurren = Calendar.getInstance();
        tvTimeToday.setText("Hôm nay");
        tvTimeToday.setText("");
        tvTimeTodayDistance.setText("Tháng :" + calendar.get(Calendar.MONTH));
        tvTimeTodayDistance.setText("");
        task = new ViewTodaysStepCountTask().execute(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
        one = new ViewTodaysAndMonthStepCountTask().execute();


        SessionManager.getInstance().setKeySaveCheck(false);
        if (!SessionManager.getInstance().getKeySaveCheck()) {
            long when = System.currentTimeMillis() + 20000L;
            AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), NotificationReceiver.class);
            intent.putExtra("myAction", "notifi");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 5555, intent, 0);
            am.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
        }


        return view;
    }

    // biều đồ cột
    private void dataBar(int i1, int i2, int i3, int i4, int i5, int i6) {

        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(2, i1));
        values.add(new BarEntry(6, i2));
        values.add(new BarEntry(10, i3));
        values.add(new BarEntry(14, i4));
        values.add(new BarEntry(18, i5));
        values.add(new BarEntry(22, i6));

        // cột y phải
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(true); // remove nó đi

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setEnabled(true);

        // line X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(1f);
        xAxis.setGranularity(4f);
        xAxis.setAxisMaximum(24f);

        //box
        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(false);

        // remove description
        Description des = barChart.getDescription();
        des.setEnabled(false);

        Legend legend = new Legend();
        legend.setEnabled(true);

//        barChart.getAxisLeft().setDrawLabels(false);
//        barChart.getXAxis().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);

        BarDataSet barDataSet = new BarDataSet(values, "Steps in Day");
        barDataSet.setColor(Color.BLACK);
        BarData barData = new BarData(barDataSet);

        barChart.setData(barData);
        barChart.invalidate();
    }

    // biểu đồ đường
    private void addtoChart() {
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setEnabled(true);
        leftAxis.setDrawTopYLabelEntry(false);

        final List<String> xLabel = new ArrayList<>();
        xLabel.add("0");
        xLabel.add("4");
        xLabel.add("8");
        xLabel.add("12");
        xLabel.add("16");
        xLabel.add("20");


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int) value % xLabel.size());
            }
        });

        CombinedData data = new CombinedData();
        LineData lineDatas = new LineData();
        lineDatas.addDataSet((ILineDataSet) dataChart());

        data.setData(lineDatas);

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        mChart.getAxisLeft().setDrawLabels(false);
        mChart.setData(data);
        mChart.invalidate();
    }

    //get dữ liệu
    private int StepinDay(int date, int month, int year) {

        final int i1 = callgooglefit(0, 4, date, month, year);
        final int i2 = callgooglefit(4, 8, date, month, year);
        final int i3 = callgooglefit(8, 12, date, month, year);
        final int i4 = callgooglefit(12, 16, date, month, year);
        final int i5 = callgooglefit(16, 20, date, month, year);
        final int i6 = callgooglefit(20, 24, date, month, year);
//        int i1 = 123;
//        int i2 = 123;
//        int i3 = 123;
//        int i4 = 123;
//        int i5 = 123;
//        int i6 = 123;

//        Log.d(TAG, "StepinDay: " + i1);
//        Log.d(TAG, "StepinDay: " + i2);
//        Log.d(TAG, "StepinDay: " + i3);
//        Log.d(TAG, "StepinDay: " + i4);
//        Log.d(TAG, "StepinDay: " + i5);
//        Log.d(TAG, "StepinDay: " + i6);
        data = new int[]{i1, i2, i3, i4, i5, i6};
//        data = new int[] {5,10,20,352,87,1};
        dataBar(i1, i2, i3, i4, i5, i6);
        sum = i1 + i2 + i3 + i4 + i5 + i6;
        addtoChart();
        return sum;
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

        DataReadResult dataReadResult = Fitness.HistoryApi.readData(MainActivity.googlefit, readRequest).await(1, TimeUnit.MINUTES);

        //Used for aggregated data
        if (dataReadResult.getBuckets().size() > 0) {
//            Log.e("History", "Number of buckets: if " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<com.google.android.gms.fitness.data.DataSet> dataSets = bucket.getDataSets();
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
                    Log.e("History", "\tField: " + field.getName() +
                            " Value: " + dp.getValue(field));
                    ret = Integer.parseInt(String.valueOf(dp.getValue(field))) + 0;
                }
            }
        }
        return ret;
    }

    private void init() {
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addApi(Fitness.HISTORY_API)
//                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
//                .addConnectionCallbacks(this)
//                .enableAutoManage(getActivity(), 0, this)
//                .build();
//        txtFull, tvSteps, tvStepToday, tvTimeToday, txtKm;
        barChart = view.findViewById(R.id.barchart);
        mChart = view.findViewById(R.id.combinedChart);
        txtFull = view.findViewById(R.id.txtFull);
        tvSteps = view.findViewById(R.id.tvSteps);
        tvStepToday = view.findViewById(R.id.tvStepToday);
        tvTimeToday = view.findViewById(R.id.tvTimeToday);
        tvStepMonth = view.findViewById(R.id.tvStepMonth);
        tvTimeTodayDistance = view.findViewById(R.id.tvTimeTodayDistance);


        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        dialog = DialogShow.showdialog(getActivity());

    }

    // dữ liệu biểu đồ đường
    private DataSet dataChart() {

        LineData d = new LineData();
        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < 6; index++) {
            entries.add(new Entry(index, data[index]));
        }

        LineDataSet set = new LineDataSet(entries, "Request Ots approved");
        set.setColor(Color.BLACK);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.BLACK);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return set;
    }

    // list ngày tháng
    private void setDataTime() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar starDate = Calendar.getInstance();
        txtFull.setText(dateFormat.format(starDate.getTime()));
        starDate.add(Calendar.MONTH, -1);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 0);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(starDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                txtFull.setText(dateFormat.format(date.getTime()));
                int day = date.get(Calendar.DATE);
                int month = date.get(Calendar.MONTH);
                int year = date.get(Calendar.YEAR);
                task = new ViewTodaysStepCountTask().execute(day, month, year);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    startJob();
//                }
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

    //check connect
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "onConnectionSuspended");
    }

    //check connect
    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended");
    }

    //check connect
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionSuspended");
    }

    @Override
    public void onPause() {
        super.onPause();
//        mGoogleApiClient.stopAutoManage(getActivity());
//        mGoogleApiClient.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //AsyncTask
    private class ViewTodaysStepCountTask extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected Integer doInBackground(Integer... integers) {
            int step = StepinDay(integers[0], integers[1], integers[2]);
            return step;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            tvSteps.setText(integer + " step");
        }
    }

    //AsyncTask
    private class ViewTodaysAndMonthStepCountTask extends AsyncTask<Integer, Void, Map<String, Integer>> {
        @Override
        protected Map<String, Integer> doInBackground(Integer... integers) {
            Calendar calendar = Calendar.getInstance();
            stepTodate = callgooglefit(0, 24, calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
            stepMonth = callgooglefitOne();
            Map<String, Integer> map = new HashMap<>();
            map.put("n1", stepMonth);
            map.put("n2", stepTodate);
            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Integer> map) {
            super.onPostExecute(map);
            if (!map.isEmpty()) {
                tvStepToday.setText("" + map.get("n2"));
                tvStepMonth.setText("" + map.get("n1"));
            }
        }
    }

    // get dữ liệu theo tháng
    private int callgooglefitOne() {

        int i = 0;
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.MONTH, -1);
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

    // gọi thông báo
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startJob() {
        ComponentName serviceComponent = new ComponentName(getActivity(), StepNotifi.class);
        JobInfo.Builder builder = new JobInfo.Builder(1, serviceComponent);
        builder.setMinimumLatency(1 * 1000); // wait at least
        builder.setOverrideDeadline(2 * 1000); // maximum delay
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        builder.setRequiresDeviceIdle(true); // device should be idle
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("abc", "123");
        builder.setExtras(bundle);
        JobScheduler jobScheduler =
                (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }
}