package com.example.diabetes.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.diabetes.R;
import com.example.diabetes.model.UserReponse.UserReponse;
import com.example.diabetes.services.APIServices;
import com.example.diabetes.services.DataService;
import com.example.diabetes.util.SharePrefs;
import com.example.diabetes.util.constant.*;
import com.example.diabetes.util.dialog.*;
import com.example.diabetes.util.listenner_change_text.addListenerOnTextChange;
import com.example.diabetes.util.validations.Validations;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private TextView txtCreateAcount, txtForgotPass;
    private TextInputLayout txtInputErrEmail, txtInputErrPass;
    private LinearLayout linearLayoutLogin;
    ;
    private String TAG = "AAA";
    private SharePrefs sharePrefs;
    private final String[] mPermissions = {Manifest.permission.ACTIVITY_RECOGNITION};
    private final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCompat.requestPermissions(this, mPermissions, REQUEST_CODE);
        init();
        isCheckUser();
    }

    private void isCheckUser() {
        sharePrefs = new SharePrefs(getApplication());
        if (sharePrefs.getUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_ride);
        }
    }

    private void listenerClicked() {
        edtEmail.addTextChangedListener(new addListenerOnTextChange(LoginActivity.this, txtInputErrEmail, 1, edtEmail));
        edtPassword.addTextChangedListener(new addListenerOnTextChange(LoginActivity.this, txtInputErrPass, 2, edtPassword));
        txtCreateAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_ride);
            }
        });

        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validations.isEmail(edtEmail.getText().toString()) == false) {
                    Toast.makeText(LoginActivity.this, "Please enter your email to retrieve your password", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), ConfirmEmailActivity.class);
                    intent.putExtra("email", edtEmail.getText().toString());
                    startActivity(intent);
                }
            }
        });

        linearLayoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = Validations.isLogin(edtEmail.getText().toString(), edtPassword.getText().toString());
                if (check) {
                    final Dialog dialog = DialogShow.showdialog(LoginActivity.this);
                    dialog.show();
                    DataService dataService = APIServices.getService();
                    Call<UserReponse> callback = dataService.login(edtEmail.getText().toString(), edtPassword.getText().toString());
                    callback.enqueue(new Callback<UserReponse>() {
                        @Override
                        public void onResponse(Call<UserReponse> call, Response<UserReponse> response) {
                            Log.d(TAG, "onResponse: login-" + response.toString());
                            if (response.isSuccessful()) {
                                if (response.body().getStatuscode() == 200) {
                                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    sharePrefs.saveUser(response.body().getData().get(0));
                                    dialog.dismiss();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_ride);
                                } else {
                                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "account incorrect", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserReponse> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, Constant.toast, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: login-" + t.toString());
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Data invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        linearLayoutLogin = findViewById(R.id.linearLayoutLogin);
        txtForgotPass = findViewById(R.id.txtForgotPass);
        txtCreateAcount = findViewById(R.id.txtCreateAcount);
        txtInputErrPass = findViewById(R.id.txtInputErrPass);
        txtInputErrEmail = findViewById(R.id.txtInputErrEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            for (int i = 0; i < mPermissions.length; i++) {

                String permissio = mPermissions[i];
                int grantResult = grantResults[i];

                if (permissio.equals(Manifest.permission.ACTIVITY_RECOGNITION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        listenerClicked();
                    } else {
                        requestPermissions(new String[]{permissio}, REQUEST_CODE);
                        Toast.makeText(this, "Please grant permissions to the app ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }
}
