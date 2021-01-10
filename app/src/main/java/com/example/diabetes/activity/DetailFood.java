package com.example.diabetes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabetes.R;
import com.example.diabetes.adapter.AdapterListFood;
import com.example.diabetes.model.DetailModel;
import com.example.diabetes.model.Menu;
import com.example.diabetes.services.APIServices;
import com.example.diabetes.services.DataService;
import com.example.diabetes.util.constant.Constant;
import com.example.diabetes.util.dialog.DialogShow;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFood extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView mRecyclerviewListFood;
    private String TAG = "DetailFood";
    private TextView mToolbarDetailFood;
    private AdapterListFood mAdapter;
    private  DetailModel mDetailModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        initView();
        init();
        listenerOnclicked();
    }

    //anh xa view
    private void initView() {
        mToolbarDetailFood = findViewById(R.id.toolbarDetailFood);
        mRecyclerviewListFood = findViewById(R.id.recyclerviewListFood);
    }

    private void listenerOnclicked() {
        findViewById(R.id.imgback).setOnClickListener(this);
    }

    private void init() {
        Intent intent  = getIntent();
        mDetailModel = (DetailModel) intent.getSerializableExtra("menu");

        getDataListFood(mDetailModel.getmIdMold(),mDetailModel.getmIdUser());
        mToolbarDetailFood.setText(mDetailModel.getmName());

        mRecyclerviewListFood.setLayoutManager(new GridLayoutManager(DetailFood.this,2));
        mRecyclerviewListFood.setHasFixedSize(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgback:
                finish();
                break;
        }
    }

    private void getDataListFood(final int idMold,final int idUser) {
        final Dialog dialog = DialogShow.showdialog(this);
        dialog.show();
        DataService dataService = APIServices.getService();
        Call<ArrayList<Menu>> call = dataService.getListFood(idMold, idUser);
        call.enqueue(new Callback<ArrayList<Menu>>() {
            @Override
            public void onResponse(Call<ArrayList<Menu>> call, Response<ArrayList<Menu>> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                dialog.dismiss();
                if (response.isSuccessful()) {
                    mAdapter = new AdapterListFood(response.body(),DetailFood.this,R.layout.layout_list_food);
                    mRecyclerviewListFood.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), Constant.toast, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Menu>> call, Throwable t) {
                dialog.dismiss();
                Log.d(TAG, "onFailure: getListFood-" + t.toString());
            }
        });
    }

    public void translateUpdate(Menu menu){
        Intent intent = new Intent(getApplicationContext(),AddFoodActivity.class);
        intent.putExtra("menu", menu);
        startActivity(intent);
        finish();
    }

}