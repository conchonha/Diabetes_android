package com.example.diabetes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabetes.R;
import com.example.diabetes.model.HealthInformationModel;
import com.example.diabetes.model.UserReponse.User;
import com.example.diabetes.services.APIServices;
import com.example.diabetes.services.DataService;
import com.example.diabetes.util.SharePrefs;
import com.example.diabetes.util.dialog.DialogShow;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtMounth,txtCreateTime;
    private EditText edtHeights,edtWeights,edtBloodPressure,edtBloodSugar,edtCPR,edtHDLC,edtLDLC;
    private EditText edtHeights1,edtWeights1,edtBloodPressure1,edtBloodSugar1,edtCPR1,edtHDLC1,edtLDLC1;
    private SharePrefs sharePrefs;
    private User user;
    private Dialog mDialog;
    private String TAG = "AAA";
    private HealthInformationModel healthInformationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_information);
        initView();
        init();
        setDataTime();
        listenerOnclicked();
    }

    private void init() {
        sharePrefs = new SharePrefs(getApplication());
        user = sharePrefs.getUser();
        mDialog = DialogShow.showdialog(HealthInformationActivity.this);
        getLastHealthInformation();
    }

    private void getLastHealthInformation() {
        mDialog.show();
        DataService dataService = APIServices.getService();
        Call<HealthInformationModel> call = dataService.getLastHealthInformation();
        call.enqueue(new Callback<HealthInformationModel>() {
            @Override
            public void onResponse(Call<HealthInformationModel> call, Response<HealthInformationModel> response) {
                mDialog.dismiss();
                Log.d(TAG, "onFailure: " + response.toString());
                if (response.isSuccessful()) {
                    healthInformationModel = response.body();
                    if(healthInformationModel != null){
                        setDataForm1(healthInformationModel);
                    }else{
                        removeForm1();
                    }
                } else {
                    Toast.makeText(HealthInformationActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    removeForm1();
                }
            }

            @Override
            public void onFailure(Call<HealthInformationModel> call, Throwable t) {
                Toast.makeText(HealthInformationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.toString());
                removeForm1();
            }
        });
    }

    private void listenerOnclicked() {
        findViewById(R.id.btnAdd).setOnClickListener(this);
        findViewById(R.id.btnXoa).setOnClickListener(this);
        findViewById(R.id.btn_update).setOnClickListener(this);
        findViewById(R.id.img_chart).setOnClickListener(this);
    }

    private void initView() {
        txtMounth = findViewById(R.id.txtMounth);
        edtHeights = findViewById(R.id.edt_heights);
        edtWeights = findViewById(R.id.edt_weights);
        edtBloodPressure = findViewById(R.id.edt_bloodPressure);
        edtBloodSugar = findViewById(R.id.edt_bloodSugar);
        edtCPR = findViewById(R.id.edt_cpr);
        edtHDLC = findViewById(R.id.edt_dhl_c);
        edtLDLC = findViewById(R.id.edt_ldl_c);

        edtHeights1 = findViewById(R.id.edt_heights1);
        edtWeights1 = findViewById(R.id.edt_weights1);
        edtBloodPressure1 = findViewById(R.id.edt_bloodPressure1);
        edtBloodSugar1 = findViewById(R.id.edt_bloodSugar1);
        edtCPR1 = findViewById(R.id.edt_cpr1);
        edtHDLC1 = findViewById(R.id.edt_hdl_c_1);
        edtLDLC1 = findViewById(R.id.edt_ldl_c_1);
        txtCreateTime = findViewById(R.id.txt_create_time);
    }

    // list ngày tháng
    private void setDataTime() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar starDate = Calendar.getInstance();
        starDate.add(Calendar.MONTH, -1);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 0);
        txtMounth.setText("月: "+((endDate.get(Calendar.MONTH)+1)));
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(starDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                int day = date.get(Calendar.DATE);
                int month = date.get(Calendar.MONTH)+1;
                int year = date.get(Calendar.YEAR);
                final String time  = year+"-"+month+"-"+day;
                Log.d(TAG, "onDateSelected: time--"+time);
                mDialog.show();

                DataService dataService = APIServices.getService();
                Call<HealthInformationModel>call = dataService.getObjectHealthInformation(time);

                call.enqueue(new Callback<HealthInformationModel>() {
                    @Override
                    public void onResponse(Call<HealthInformationModel> call, Response<HealthInformationModel> response) {
                        Log.d(TAG, "onResponse: "+response.toString());
                        mDialog.dismiss();
                        if (response.isSuccessful()) {
                            healthInformationModel = response.body();
                            if(healthInformationModel != null){
                                setDataForm1(healthInformationModel);
                            }else{
                                removeForm1();
                                healthInformationModel = null;
                            }
                        } else {
                            Toast.makeText(HealthInformationActivity.this, "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
                            removeForm1();
                            healthInformationModel = null;
                        }
                    }

                    @Override
                    public void onFailure(Call<HealthInformationModel> call, Throwable t) {
                        Toast.makeText(HealthInformationActivity.this, "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
                        removeForm1();
                        mDialog.dismiss();
                        healthInformationModel = null;
                    }
                });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAdd:
                insertAndUpdate(edtHeights.getText().toString(),
                        edtWeights.getText().toString(),edtBloodPressure.getText().toString(),edtBloodSugar.getText().toString(),
                        edtCPR.getText().toString(),edtHDLC.getText().toString(),edtLDLC.getText().toString(),getTime(),user.getId().toString(),0);
                break;
            case R.id.btnXoa:
                delete();
                break;
            case R.id.btn_update:
                if(healthInformationModel != null && healthInformationModel.getId() != null){
                    insertAndUpdate(edtHeights1.getText().toString(),
                            edtWeights1.getText().toString(),edtBloodPressure1.getText().toString(),edtBloodSugar1.getText().toString(),
                            edtCPR1.getText().toString(),edtHDLC1.getText().toString(),edtLDLC1.getText().toString(),getTime(),user.getId().toString(),healthInformationModel.getId());
                }else{
                    Toast.makeText(this, "Không có dữ liệu nào để update", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_chart:
                startActivity(new Intent(getApplicationContext(),BloodPressureChartActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_ride);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(RESULT_OK);
        return super.onKeyDown(keyCode, event);
    }

    public static String getTime(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mounth = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);

        String time = year + "-"+mounth + "-"+date;
        return time;
    }

    public void removeForm1(){
        txtCreateTime.setText("");
        edtHeights1.setText("");
        edtWeights1.setText("");
        edtBloodPressure1.setText("");
        edtBloodSugar1.setText("");
        edtCPR1.setText("");
        edtHDLC1.setText("");
        edtLDLC1.setText("");
    }

    public void setDataForm1(HealthInformationModel model){
        if(model !=  null){
            txtCreateTime.setText(model.getTime());
            edtHeights1.setText(model.getHeights().toString());
            edtWeights1.setText(model.getWeights().toString());
            edtBloodPressure1.setText(model.getBloodPressure().toString());
            edtBloodSugar1.setText(model.getBloodSugar().toString());
            edtCPR1.setText(model.getCPR().toString());
            edtHDLC1.setText(model.getHDLC().toString());
            edtLDLC1.setText(model.getLDLC().toString());
        }else{
            removeForm1();
        }
    }

    private void insertAndUpdate(String height, String weight, String bloodPressure, String bloodSugar, String cpr, String hdlc,
                                 String ldlc, String time, String id, final int check){
        mDialog.show();
        DataService service = APIServices.getService();
        Call<HealthInformationModel>call = service.insert(height,weight,bloodPressure,bloodSugar,cpr,hdlc,ldlc,time,id,check);
        call.enqueue(new Callback<HealthInformationModel>() {
            @Override
            public void onResponse(Call<HealthInformationModel> call, Response<HealthInformationModel> response) {
                Log.d("AAA", "onResponse: "+response.toString());
                mDialog.dismiss();
                if(response.isSuccessful()){
                    if(check == 0){
                        Toast.makeText(HealthInformationActivity.this, "Insert Success", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(HealthInformationActivity.this, "Update Success", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    startActivity(getIntent());
                }else{
                    Toast.makeText(HealthInformationActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HealthInformationModel> call, Throwable t) {
                Log.d("AAA", "onFailure: "+t.toString());
                mDialog.dismiss();
                Toast.makeText(HealthInformationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void delete(){
        if(healthInformationModel != null && healthInformationModel.getTime() != null){
            DataService dataService = APIServices.getService();
            Call<String>call1 = dataService.deleteObject(healthInformationModel.getTime());

            call1.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful() && response.body().equals("Success")){
                        Toast.makeText(HealthInformationActivity.this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }else{
                        Toast.makeText(HealthInformationActivity.this, "Xoá Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(HealthInformationActivity.this, "Xoá Thất Bại", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "Vui lòng chọn ngày cần xoá", Toast.LENGTH_SHORT).show();
        }
    }
}