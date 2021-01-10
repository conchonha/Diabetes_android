package com.example.diabetes.util;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.example.diabetes.model.HealthInformationModel;
import com.example.diabetes.model.UserReponse.User;
import com.google.gson.Gson;

public class SharePrefs {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson = new Gson();

    public SharePrefs(Application activity){
        sharedPreferences = activity.getSharedPreferences("datalogin",activity.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public User getUser(){
        if(sharedPreferences.getString("user","").equals("")){
            return null;
        }
        return gson.fromJson(sharedPreferences.getString("user",""),User.class);
    }

    public void saveUser(User user){
        editor.putString("user",gson.toJson(user)).commit();
    }

    public void clearUser(){
        editor.remove("user").commit();
    }

    public void saveHealthInformation(HealthInformationModel model){
        editor.putString("health",gson.toJson(model)).commit();
    }

    public HealthInformationModel getHealthInformation(){
        if(sharedPreferences.getString("health","").equals("")){
            return null;
        }
        return gson.fromJson(sharedPreferences.getString("health",""),HealthInformationModel.class);
    }
}
