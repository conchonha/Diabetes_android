package com.example.diabetes.adapter;

import android.app.Dialog;
import android.content.Context;
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

public class AdapterListFoodOffer extends RecyclerView.Adapter <AdapterListFoodOffer.Viewhodler>{
    private ArrayList<Menu>arrayList;
    private Context context;
    private int layout;
    private boolean check;
    private String TAG = "AAA";

    public AdapterListFoodOffer(ArrayList<Menu> arrayList, Context context, int layout,boolean check) {
        this.arrayList = arrayList;
        this.context = context;
        this.layout = layout;
        this.check = check;
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
        holder.txtName.setText(arrayList.get(position).getNamefood());
        if(arrayList.get(position).getImage() != null){
            Picasso.with(context).load(APIServices.urlImage+arrayList.get(position).getImage()).into(holder.imgFood);
        }
        holder.imgMenu.setVisibility(View.GONE);
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
