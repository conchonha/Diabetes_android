package com.example.diabetes.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabetes.R;
import com.example.diabetes.adapter.AdapterListFoodOffer;
import com.example.diabetes.model.Menu;
import com.example.diabetes.services.APIServices;
import com.example.diabetes.services.DataService;
import com.example.diabetes.util.constant.Constant;
import com.example.diabetes.util.dialog.DialogShow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFoodOffer extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private String TAG = "AAA";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food_offer,container,false);
        init();
        getDataFoodOffer();
        return view;
    }

    private void getDataFoodOffer() {
        final Dialog dialog = DialogShow.showdialog(view.getContext());
        dialog.show();
        DataService dataService = APIServices.getService();
        Call<ArrayList<Menu>>call = dataService.getDataFoodOffer();
        call.enqueue(new Callback<ArrayList<Menu>>() {
            @Override
            public void onResponse(Call<ArrayList<Menu>> call, Response<ArrayList<Menu>> response) {
                Log.d(TAG, "onResponse: getDataFoodOffer-"+response.toString());
                if(response.isSuccessful()){
                    ArrayList<Menu>arrayList = response.body();
                    AdapterListFoodOffer adapterListFood = new AdapterListFoodOffer(arrayList, getActivity(),R.layout.layout_list_food,true);
                    Collections.shuffle(arrayList);
                    recyclerView.setAdapter(adapterListFood);
                    adapterListFood.notifyDataSetChanged();
                    dialog.dismiss();
                }else{
                    Log.d(TAG, "onResponse: response-"+response.message());
                    dialog.dismiss();
                    Toast.makeText(view.getContext(), Constant.toast, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Menu>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(view.getContext(), Constant.toast, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: getDataFoodOffer-"+t.toString());
            }
        });
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),2));
    }
}
