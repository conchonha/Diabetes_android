package com.example.diabetes.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.diabetes.R;
import com.example.diabetes.model.HealthInformationModel;
import com.example.diabetes.services.APIServices;
import com.example.diabetes.services.DataService;
import com.example.diabetes.util.Helpers;
import com.example.diabetes.util.dialog.DialogShow;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BloodPressureChartActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<BarEntry> arrayList = new ArrayList<>();
    private Dialog mDialog;
    private ImageView mImgMenu;
    private List<HealthInformationModel> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_chart);
        initView();
        init();
        listenerOnclicked();
    }

    private void listenerOnclicked() {
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.img_menu).setOnClickListener(this);
    }

    private void showChart(String str){
        BarChart barChart = findViewById(R.id.barChart);

        BarDataSet barDataSet = new BarDataSet(arrayList, str);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText(str+" Chart");
        barChart.animateY(2000);
    }


    private void init() {
        mDialog = DialogShow.showdialog(BloodPressureChartActivity.this);
        mDialog.show();

        DataService dataService = APIServices.getService();
        Call<List<HealthInformationModel>> call = dataService.getBloodPressureChart();

        call.enqueue(new Callback<List<HealthInformationModel>>() {
            @Override
            public void onResponse(Call<List<HealthInformationModel>> call, Response<List<HealthInformationModel>> response) {
                mDialog.dismiss();
                mList = response.body();
                if (response.isSuccessful()) {
                    for (int i = 0; i < mList.size(); i++) {
                        arrayList.add(new BarEntry(i + 1, mList.get(i).getWeights()));
                    }
                    showChart("Weights");
                } else {
                    Helpers.showAlertDialog(response.message(), BloodPressureChartActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<HealthInformationModel>> call, Throwable t) {
                mDialog.dismiss();
                Helpers.showAlertDialog(t.getMessage(), BloodPressureChartActivity.this);
            }
        });
    }

    private void initView() {
        mImgMenu = findViewById(R.id.img_menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.img_menu:
                PopupMenu popupMenu = new PopupMenu(this,mImgMenu);
                popupMenu.inflate(R.menu.menu_chart);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.weights:
                                arrayList.clear();
                                for (int i = 0; i < mList.size(); i++) {
                                    arrayList.add(new BarEntry(i + 1, mList.get(i).getWeights()));
                                }
                                showChart("Weights");
                                break;
                            case R.id.height:
                                arrayList.clear();
                                for (int i = 0; i < mList.size(); i++) {
                                    arrayList.add(new BarEntry(i + 1, mList.get(i).getHeights()));
                                }
                                showChart("Height");
                                break;
                            case R.id.boolPressure:
                                arrayList.clear();
                                for (int i = 0; i < mList.size(); i++) {
                                    arrayList.add(new BarEntry(i + 1, mList.get(i).getBloodPressure()));
                                }
                                showChart("Bool Pressure");
                                break;
                            case R.id.bloodSugar:
                                arrayList.clear();
                                for (int i = 0; i < mList.size(); i++) {
                                    arrayList.add(new BarEntry(i + 1, mList.get(i).getBloodSugar()));
                                }
                                showChart("Blood Sugar");
                                break;
                            case R.id.cpr:
                                arrayList.clear();
                                for (int i = 0; i < mList.size(); i++) {
                                    arrayList.add(new BarEntry(i + 1, mList.get(i).getCPR()));
                                }
                                showChart("CPR");
                                break;
                            case R.id.hdl:
                                arrayList.clear();
                                for (int i = 0; i < mList.size(); i++) {
                                    arrayList.add(new BarEntry(i + 1, mList.get(i).getHDLC()));
                                }
                                showChart("HDL-C");
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
                break;
        }
    }
}