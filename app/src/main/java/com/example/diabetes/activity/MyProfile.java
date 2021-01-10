package com.example.diabetes.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diabetes.R;
import com.example.diabetes.model.HealthInformationModel;
import com.example.diabetes.model.UserReponse.User;
import com.example.diabetes.model.UserReponse.UserReponse;
import com.example.diabetes.services.APIServices;
import com.example.diabetes.services.DataService;
import com.example.diabetes.util.SharePrefs;
import com.example.diabetes.util.constant.Constant;
import com.example.diabetes.util.dialog.DialogShow;
import com.google.gson.Gson;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfile extends AppCompatActivity implements View.OnClickListener {
    private EditText edtGender, edtEmail, edtFullName, edtAge;
    private TextView txtWeight, txtHeight, txtBloodPressure, txtBloodSugar, txtCpr, txtHdlC, txtLdlC, txtDate;
    private Dialog mDialog;
    private String TAG = "AAA";
    private User mUser;
    private HealthInformationModel healthInformationModel;
    private SharePrefs sharePrefs;
    private final int REQUEST_CODE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initView();
        init();
        listenerOnclicked();
    }

    private void listenerOnclicked() {
        findViewById(R.id.txt_update).setOnClickListener(this);
        findViewById(R.id.txt_setting).setOnClickListener(this);
        txtDate.setOnClickListener(this);
        findViewById(R.id.imgback).setOnClickListener(this);
    }

    private void init() {
        sharePrefs = new SharePrefs(getApplication());
        mUser = sharePrefs.getUser();
        healthInformationModel = sharePrefs.getHealthInformation();
        if (mUser != null) {
            edtAge.setText(mUser.getAge().toString());
            edtGender.setText(mUser.getSex().toString().equals("0") ? "男" : "女");
            txtDate.setText(mUser.getDayofbirth());
            edtEmail.setText(mUser.getEmail());
        }
        getLastHealthInformation();
    }

    private void getLastHealthInformation() {
        mDialog = DialogShow.showdialog(MyProfile.this);
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
                        txtWeight.setText(healthInformationModel.getWeights().toString());
                        txtHeight.setText(healthInformationModel.getHeights().toString());
                        txtBloodPressure.setText(healthInformationModel.getBloodPressure().toString());
                        txtBloodSugar.setText(healthInformationModel.getBloodSugar().toString());
                        txtCpr.setText(healthInformationModel.getCPR().toString());
                        txtLdlC.setText(healthInformationModel.getLDLC().toString());
                    }
                } else {
                    Toast.makeText(MyProfile.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HealthInformationModel> call, Throwable t) {
                Toast.makeText(MyProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    //ánh xạ view
    private void initView() {
        edtEmail = findViewById(R.id.edt_email);
        edtFullName = findViewById(R.id.edt_full_name);
        edtGender = findViewById(R.id.edt_gender);
        txtDate = findViewById(R.id.txt_date);
        edtAge = findViewById(R.id.edt_age);

        txtWeight = findViewById(R.id.txt_weight);
        txtHeight = findViewById(R.id.txt_height);
        txtBloodPressure = findViewById(R.id.txt_blood_pressure);
        txtBloodSugar = findViewById(R.id.txt_blood_sugar);
        txtCpr = findViewById(R.id.txt_cpr);
        txtHdlC = findViewById(R.id.txt_hdl_c);
        txtLdlC = findViewById(R.id.txt_ldlc);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_update:
                updateInforUser();
                break;
            case R.id.txt_setting:
                startActivityForResult(new Intent(getApplicationContext(), HealthInformationActivity.class),1111);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_ride);
                break;
            case R.id.txt_date:
                Calendar Calenda = Calendar.getInstance();
                int year = Calenda.get(Calendar.YEAR);
                int mounth = Calenda.get(Calendar.MONTH) + 1;
                int date = Calenda.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MyProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, year, mounth, date);
                datePickerDialog.show();
                break;
            case R.id.imgback:
                finish();
                break;
        }
    }

    private void updateInforUser() {
        mDialog = DialogShow.showdialog(MyProfile.this);
        mDialog.show();
        DataService dataService = APIServices.getService();
        Call<UserReponse> call = dataService.register(edtFullName.getText().toString(), edtEmail.getText().toString(), mUser.getPassword(), edtAge.getText().toString(), txtDate.getText().toString(), edtGender.getText().toString().equals("男") ? "0" : "1", 1);
        call.enqueue(new Callback<UserReponse>() {
            @Override
            public void onResponse(Call<UserReponse> call, Response<UserReponse> response) {
                Log.d(TAG, "onResponse: register-" + response.toString());
                mDialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + new Gson().toJson(response.body()));
                    if (response.body().getStatuscode() == 200) {
                        Toast.makeText(MyProfile.this, "Susscess", Toast.LENGTH_SHORT).show();
                        mUser.setFullname(edtFullName.getText().toString());
                        mUser.setAge(Integer.parseInt(edtAge.getText().toString()));
                        mUser.setDayofbirth(txtDate.getText().toString());
                        mUser.setSex(edtGender.getText().toString().equals("男") ? 0 : 1);
                    } else {
                        Toast.makeText(MyProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyProfile.this, Constant.toast, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserReponse> call, Throwable t) {
                Toast.makeText(MyProfile.this, Constant.toast, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
                Log.d(TAG, "onFailure: register-" + t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            finish();
            startActivity(getIntent());
        }
    }
}