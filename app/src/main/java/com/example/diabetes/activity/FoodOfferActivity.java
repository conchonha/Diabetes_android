package com.example.diabetes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.diabetes.R;

public class FoodOfferActivity extends AppCompatActivity {
    private ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_offer);
        init();
        listenerOnclicked();
    }

    private void listenerOnclicked() {
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
        }

        });
    }

    private void init() {
        imgback = findViewById(R.id.imgback);
    }
}