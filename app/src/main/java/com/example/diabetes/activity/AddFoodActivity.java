package com.example.diabetes.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabetes.R;
import com.example.diabetes.model.DetailModel;
import com.example.diabetes.model.Menu;
import com.example.diabetes.model.UserReponse.User;
import com.example.diabetes.services.APIServices;
import com.example.diabetes.services.DataService;
import com.example.diabetes.util.SharePrefs;
import com.example.diabetes.util.constant.Constant;
import com.example.diabetes.util.dialog.DialogShow;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFoodActivity extends AppCompatActivity {
    private ImageView imgback,imgInserFood,imgDrop,imgImageThucDon;
    private TextView txtTypeFood,toolbarManagerUser,txtNameButtonAdd;
    private LinearLayout linearLayoutAddFood;
    private int REQUEST_CODE = 1234;
    private Bitmap bitmap;
    private int id_mold = 1;
    private EditText edtNameFood,edtGam;
    private String byteImg = "";
    private String TAG = "AAA";
    private Menu menu;
    private User user;
    private SharePrefs sharePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        initView();
        init();
    }

    private void initView() {
        sharePrefs = new SharePrefs(getApplication());
        txtNameButtonAdd = findViewById(R.id.txtNameButtonAdd);
        toolbarManagerUser = findViewById(R.id.toolbarManagerUser);
        edtNameFood = findViewById(R.id.edtNameFood);
        txtTypeFood = findViewById(R.id.txtTypeFood);
        imgDrop = findViewById(R.id.imgDrop);
        imgImageThucDon = findViewById(R.id.imgImageThucDon);
        linearLayoutAddFood = findViewById(R.id.linearLayoutAddFood);
        imgback = findViewById(R.id.imgback);
        imgInserFood = findViewById(R.id.imgInserFood);
        edtGam = findViewById(R.id.edt_gam);
    }

    private void listenerOnclicked(final int check,final int id_menu) {
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgInserFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_CODE);
                }
            }
        });

        linearLayoutAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int date =calendar.get(Calendar.DATE);
                int mounth = calendar.get(Calendar.MONTH);

                String day = year+"-"+mounth+"-"+(date+1);
                String name = edtNameFood.getText().toString();

                if(bitmap != null){
                   byteImg = imageToStringForResultf(bitmap);
                }

                if(name.equals("")){
                    Toast.makeText(AddFoodActivity.this, "Please enter the name of the dish to add", Toast.LENGTH_SHORT).show();
                }else if(edtGam.getText().equals("")){
                    Toast.makeText(AddFoodActivity.this, "Please enter the gam of the dish to add", Toast.LENGTH_SHORT).show();
                }else{
                    final Dialog dialog = DialogShow.showdialog(AddFoodActivity.this);
                    dialog.show();

                    final User user = sharePrefs.getUser();

                    DataService dataService = APIServices.getService();
                    Call<String>call = dataService.postFood(id_mold,user.getId(),name,byteImg,Integer.parseInt(edtGam.getText().toString()),day,check,id_menu);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d(TAG, "onResponse: postFood-"+response.toString());
                            if(response.isSuccessful()){
                               if(response.body().equals("Sussces")){
                                   Toast.makeText(AddFoodActivity.this, "Sussces", Toast.LENGTH_SHORT).show();
                                   if(check == 1){
                                       DetailModel detailModel = new DetailModel(txtTypeFood.getText().toString(),menu.getIdMold(),user.getId());
                                       Intent intent = new Intent(getApplicationContext(),DetailFood.class);
                                       intent.putExtra("menu",detailModel);
                                       startActivity(intent);
                                       finish();
                                   }else{
                                       setResult(RESULT_OK);
                                       finish();
                                   }
                               }else{
                                   dialog.dismiss();
                                   Toast.makeText(AddFoodActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                               }
                            }else{
                                Toast.makeText(AddFoodActivity.this, Constant.toast, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(AddFoodActivity.this, Constant.toast, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        imgDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(AddFoodActivity.this,imgDrop);
                popupMenu.getMenuInflater().inflate(R.menu.menu_add,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.idHaveBreakfast:
                                txtTypeFood.setText("朝食");
                                id_mold = 1;
                                break;
                            case R.id.idLunch:
                               id_mold = 2;
                                txtTypeFood.setText("昼食");
                                break;
                            case R.id.idDinner:
                                id_mold = 3;
                                txtTypeFood.setText("夕食");
                                break;
                            case R.id.idOther:
                                id_mold = 4;
                                txtTypeFood.setText("おすすめ");
                                break;
                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode ==RESULT_OK && data != null){
                Bundle extras = data.getExtras();
                bitmap= (Bitmap) extras.get("data");
                imgImageThucDon.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        Intent intent = getIntent();
        if(intent.hasExtra("menu")){
            menu = (Menu) intent.getSerializableExtra("menu");
            txtNameButtonAdd.setText("Update");
            toolbarManagerUser.setText("Update Food");
            if(menu.getImage()!=null){
                Picasso.with(getApplicationContext()).load(APIServices.urlImage+menu.getImage()).into(imgImageThucDon);
            }
            edtNameFood.setText(menu.getNamefood());
            listenerOnclicked(1,menu.getId());
        }else{
            listenerOnclicked(0,0);
        }
    }

    private String imageToStringForResultf(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[]hinhanh=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(hinhanh,Base64.DEFAULT);
    }

}