package com.example.diabetes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Menu implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_mold")
    @Expose
    private Integer idMold;
    @SerializedName("id_user")
    @Expose
    private Integer idUser;
    @SerializedName("namefood")
    @Expose
    private String namefood;
    @SerializedName("image")
    @Expose
    private Object image;
    @SerializedName("gam")
    @Expose
    private int gam;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("status")
    @Expose
    private Object status;
    @SerializedName("mold")
    @Expose
    private String mold;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdMold() {
        return idMold;
    }

    public void setIdMold(Integer idMold) {
        this.idMold = idMold;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public int getGam() {
        return gam;
    }

    public void setGam(int gam) {
        this.gam = gam;
    }

    public String getNamefood() {
        return namefood;
    }

    public void setNamefood(String namefood) {
        this.namefood = namefood;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getMold() {
        return mold;
    }

    public void setMold(String mold) {
        this.mold = mold;
    }

}