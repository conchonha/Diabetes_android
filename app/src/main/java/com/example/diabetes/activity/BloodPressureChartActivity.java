package com.example.diabetes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.example.diabetes.R;
import com.example.diabetes.model.HealthInformationModel;
import com.example.diabetes.services.APIServices;
import com.example.diabetes.services.DataService;
import com.example.diabetes.util.Helpers;
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

public class BloodPressureChartActivity extends AppCompatActivity {
    private ArrayList<BarEntry>arrayList =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_chart);

        DataService dataService = APIServices.getService();
        Call<List<HealthInformationModel>>call = dataService.getBloodPressureChart();

        call.enqueue(new Callback<List<HealthInformationModel>>() {
            @Override
            public void onResponse(Call<List<HealthInformationModel>> call, Response<List<HealthInformationModel>> response) {
                if(response.isSuccessful()){
                    if(response.body().size() > 0){
                        for (int i = 0; i < response.body().size() ; i++){
                            arrayList.add(new BarEntry(i+1,response.body().get(i).getBloodPressure()));
                        }
                        BarChart barChart = findViewById(R.id.barChart);

                        BarDataSet barDataSet = new BarDataSet(arrayList,"blood pressure");
                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        barDataSet.setValueTextColor(Color.BLACK);
                        barDataSet.setValueTextSize(16f);

                        BarData barData = new BarData(barDataSet);

                        barChart.setFitBars(true);
                        barChart.setData(barData);
                        barChart.getDescription().setText("Blood Pressure Chart");
                        barChart.animateY(2000);
                    }else{
                        Helpers.showAlertDialog("Không tìm thấy kết quả nào",BloodPressureChartActivity.this);
                        close();
                    }
                }else{
                    Toast.makeText(BloodPressureChartActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    close();
                }
            }

            @Override
            public void onFailure(Call<List<HealthInformationModel>> call, Throwable t) {
                Toast.makeText(BloodPressureChartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                close();
            }
        });

    }
    private void close(){
        try {
            Thread.sleep(3000);
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}