package com.example.diabetes.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.diabetes.R;
import com.example.diabetes.activity.AddFoodActivity;
import com.example.diabetes.activity.DetailFood;
import com.example.diabetes.activity.FoodOfferActivity;
import com.example.diabetes.activity.LoginActivity;
import com.example.diabetes.model.DetailModel;
import com.example.diabetes.model.Menu;
import com.example.diabetes.model.UserReponse.User;
import com.example.diabetes.services.APIServices;
import com.example.diabetes.services.DataService;
import com.example.diabetes.util.SharePrefs;
import com.example.diabetes.util.constant.Constant;
import com.example.diabetes.util.dialog.DialogShow;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class FragmentFood extends Fragment implements View.OnClickListener {
    private View view;
    private String TAG = "FragmentFood";
    private ArrayList<Menu> mListMenuMorning = new ArrayList<>();
    private ArrayList<Menu> mListMenuLunch = new ArrayList<>();
    private ArrayList<Menu> mListMenuDinner = new ArrayList<>();
    private TextView mTxtMorning,mTxtLunch,mTxtDinner;
    private User mUser;
    private int REQUEST_CODE = 123;
    private SharePrefs sharePrefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food, container, false);
        initView();
        init();
        listennerOnclicked();
        return view;
    }

    private void initView() {
        mTxtMorning = view.findViewById(R.id.txt_morning);
        mTxtLunch = view.findViewById(R.id.txt_lunch);
        mTxtDinner = view.findViewById(R.id.txt_dinner);
    }

    private void init() {
        sharePrefs = new SharePrefs(getActivity().getApplication());
        mUser = sharePrefs.getUser();
        getDataListFood(1);
        getDataListFood(2);
        getDataListFood(3);
    }

    private void listennerOnclicked() {
        view.findViewById(R.id.imgAdd).setOnClickListener(this);
        view.findViewById(R.id.imageFoodMoring).setOnClickListener(this);
        view.findViewById(R.id.imgFoodDinner).setOnClickListener(this);
        view.findViewById(R.id.imgFoodLunch).setOnClickListener(this);
        view.findViewById(R.id.imgFoodOffer).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAdd:
                startActivityForResult(new Intent(getContext(), AddFoodActivity.class),REQUEST_CODE);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_ride);
                break;
            case R.id.imageFoodMoring:
                startActivity(new Intent(getContext(), DetailFood.class).putExtra("menu", new DetailModel("Food Morning",1, mUser.getId())));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_ride);
                break;
            case R.id.imgFoodLunch:
                startActivity(new Intent(getContext(), DetailFood.class).putExtra("menu", new DetailModel("Food Lunch",2, mUser.getId())));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_ride);
                break;
            case R.id.imgFoodDinner:
                startActivity(new Intent(getContext(), DetailFood.class).putExtra("menu", new DetailModel("Food Dinner",3, mUser.getId())));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_ride);
                break;
            case R.id.imgFoodOffer:
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setMessage("suggested dishes for you");
                dialog.setTitle("Food is suggestions");
                dialog.setIcon(R.drawable.ic_help);
                dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), FoodOfferActivity.class));
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_ride);
                    }
                });

                dialog.show();
                break;
        }
    }

    private void getDataListFood(final int id_mold) {
        final Dialog dialog = DialogShow.showdialog(getActivity());
        dialog.show();
        DataService dataService = APIServices.getService();
        Call<ArrayList<Menu>> call = dataService.getListFood(id_mold, mUser.getId());
        call.enqueue(new Callback<ArrayList<Menu>>() {
            @Override
            public void onResponse(Call<ArrayList<Menu>> call, Response<ArrayList<Menu>> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                dialog.dismiss();
                if (response.isSuccessful()) {
                    switch (id_mold) {
                        case 1:
                            mListMenuMorning = response.body();
                            mTxtMorning.setText("朝食 ("+TotalGam(mListMenuMorning)+"g)");
                            break;
                        case 2:
                            mListMenuLunch = response.body();
                            mTxtLunch.setText("昼食("+TotalGam(mListMenuLunch)+"g)");
                            break;
                        case 3:
                            mListMenuDinner = response.body();
                            mTxtDinner.setText("夕食 ("+TotalGam(mListMenuDinner)+"g)");
                            break;
                    }
                } else {
                    Toast.makeText(getContext(), Constant.toast, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Menu>> call, Throwable t) {
                dialog.dismiss();
                Log.d(TAG, "onFailure: getListFood-" + t.toString());
            }
        });
    }

    private int TotalGam(ArrayList<Menu>list){
        int tong = 0;
        for (Menu menu : list){
            tong += menu.getGam();
        }
        return tong;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            init();
        }
    }
}
