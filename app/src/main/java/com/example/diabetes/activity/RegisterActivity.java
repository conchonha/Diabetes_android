package com.example.diabetes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.diabetes.R;
import com.example.diabetes.model.UserReponse.UserReponse;
import com.example.diabetes.services.APIServices;
import com.example.diabetes.services.DataService;
import com.example.diabetes.util.SharePrefs;
import com.example.diabetes.util.dialog.DialogShow;
import com.example.diabetes.util.listenner_change_text.addListenerOnTextChange;
import com.example.diabetes.util.validations.Validations;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private CheckBox checkboxMale, checkboxGir;
    String sex = "0";
    private EditText edtAge, edtFullName, edtDateOfBirth, edtEmail, edtPassword;
    private TextInputLayout textInputLayout;
    private ImageView imgback;
    private Button btnRegister;
    private String TAG = "AAA";
    private SharePrefs sharePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        listenerClicked();
    }

    private void listenerClicked() {
        edtFullName.addTextChangedListener(new addListenerOnTextChange(RegisterActivity.this, textInputLayout, 3, edtFullName));
        edtAge.addTextChangedListener(new addListenerOnTextChange(RegisterActivity.this, textInputLayout, 4, edtAge));
        edtDateOfBirth.addTextChangedListener(new addListenerOnTextChange(RegisterActivity.this, textInputLayout, 5, edtDateOfBirth));
        edtEmail.addTextChangedListener(new addListenerOnTextChange(RegisterActivity.this, textInputLayout, 6, edtEmail));
        edtPassword.addTextChangedListener(new addListenerOnTextChange(RegisterActivity.this, textInputLayout, 7, edtPassword));

        edtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edtDateOfBirth.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        checkboxMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex = "0";
                checkboxGir.setChecked(false);
                Log.d("AAA", "sex: " + sex);
            }
        });

        checkboxGir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex = "1";
                checkboxMale.setChecked(false);
                Log.d("AAA", "sex: " + sex);
            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_ride);
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Dialog dialog = DialogShow.showdialog(RegisterActivity.this);
                dialog.show();

                String fullName = edtFullName.getText().toString();
                String age = edtAge.getText().toString();
                String dateOfBirth = edtDateOfBirth.getText().toString();
                final String email = edtEmail.getText().toString();
                final String pass = edtPassword.getText().toString();

                boolean check = Validations.isRegister(fullName, age, dateOfBirth, email, pass);

                if (check) {
                    DataService dataService = APIServices.getService();
                    Call<UserReponse> callback = dataService.register(fullName, email, pass, age, dateOfBirth, sex, 0);
                    callback.enqueue(new Callback<UserReponse>() {
                        @Override
                        public void onResponse(Call<UserReponse> call, Response<UserReponse> response) {
                            Log.d(TAG, "onResponse: register-" + response.toString());
                            if (response.isSuccessful()) {
                                if (response.body().getStatuscode() == 200) {
                                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    sharePrefs.saveUser(response.body().getData().get(0));
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_ride);
                                } else {
                                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, "registration failed", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserReponse> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this, "Err register, Please check data", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: register-" + t.toString());
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Data invalid", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });
    }

    private void init() {
        sharePrefs = new SharePrefs(getApplication());
        btnRegister = findViewById(R.id.btnRegister);
        imgback = findViewById(R.id.imgback);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtDateOfBirth = findViewById(R.id.edtDateOfBirth);
        edtFullName = findViewById(R.id.edtFullName);
        edtAge = findViewById(R.id.edtAge);
        checkboxGir = findViewById(R.id.checkboxGir);
        checkboxMale = findViewById(R.id.checkboxMale);
        checkboxMale.setChecked(true);
    }
}