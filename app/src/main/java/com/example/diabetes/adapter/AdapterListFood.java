package com.example.diabetes.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabetes.R;
import com.example.diabetes.activity.AddFoodActivity;
import com.example.diabetes.activity.DetailFood;
import com.example.diabetes.model.Menu;
import com.example.diabetes.services.APIServices;
import com.example.diabetes.services.DataService;
import com.example.diabetes.util.constant.Constant;
import com.example.diabetes.util.dialog.DialogShow;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterListFood extends RecyclerView.Adapter <AdapterListFood.Viewhodler>{
    private ArrayList<Menu>arrayList;
    private DetailFood context;
    private int layout;
    private String TAG = "AAA";

    public AdapterListFood(ArrayList<Menu> arrayList, DetailFood context, int layout) {
        this.arrayList = arrayList;
        this.context = context;
        this.layout = layout;
    }

    @NonNull
    @Override
    public Viewhodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context,layout,null);
        return new Viewhodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewhodler holder, final int position) {
        holder.txtDate.setText(arrayList.get(position).getDay());
        holder.txtName.setText(arrayList.get(position).getNamefood()+"( "+arrayList.get(position).getGam()+" )g");
        if(arrayList.get(position).getImage() != null){
            Picasso.with(context).load(APIServices.urlImage+arrayList.get(position).getImage()).into(holder.imgFood);
        }

        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,holder.imgMenu);
                popupMenu.inflate(R.menu.menu_show);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.id_delete){
                            Toast.makeText(context, arrayList.get(position).getId()+"", Toast.LENGTH_SHORT).show();
                            final Dialog dialog = DialogShow.showdialog(context);
                            dialog.show();

                            DataService dataService = APIServices.getService();
                            Call<String>call = dataService.deleteFood(arrayList.get(position).getId());
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Log.d(TAG, "onResponse: deleteFood-"+response.toString());
                                    if(response.isSuccessful()){
                                        Log.d(TAG, "onResponse: giatri_id: "+response.body());
                                        if(response.body().equals("Sussces")){
                                            context.finish();
                                            context.startActivity(context.getIntent());
                                            dialog.dismiss();
                                            Toast.makeText(context, "Susscess", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(context, Constant.toast, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }else{
                                        Toast.makeText(context, Constant.toast, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d(TAG, "onFailure: deleteFood-"+t.toString());
                                    dialog.dismiss();
                                    Toast.makeText(context, "Delete err.Please come back later", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if(menuItem.getItemId() == R.id.id_update){
                           context.translateUpdate(arrayList.get(position));
                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Viewhodler extends  RecyclerView.ViewHolder{
        private TextView txtDate,txtName;
        private ImageView imgMenu;
        private RoundedImageView imgFood;
        public Viewhodler(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtName = itemView.findViewById(R.id.txtName);
            imgMenu = itemView.findViewById(R.id.imgMenu);
            imgFood = itemView.findViewById(R.id.imgFood);
        }
    }
}
