package com.example.diabetes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HealthInformationModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("heights")
    @Expose
    private Integer heights;
    @SerializedName("weights")
    @Expose
    private Integer weights;
    @SerializedName("bloodPressure")
    @Expose
    private Integer bloodPressure;
    @SerializedName("bloodSugar")
    @Expose
    private Integer bloodSugar;
    @SerializedName("CPR")
    @Expose
    private Integer cPR;
    @SerializedName("HDL_C")
    @Expose
    private Integer hDLC;
    @SerializedName("LDL_C")
    @Expose
    private Integer lDLC;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("id_user")
    @Expose
    private Integer idUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHeights() {
        return heights;
    }

    public void setHeights(Integer heights) {
        this.heights = heights;
    }

    public Integer getWeights() {
        return weights;
    }

    public void setWeights(Integer weights) {
        this.weights = weights;
    }

    public Integer getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(Integer bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Integer getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Integer bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public Integer getCPR() {
        return cPR;
    }

    public void setCPR(Integer cPR) {
        this.cPR = cPR;
    }

    public Integer getHDLC() {
        return hDLC;
    }

    public void setHDLC(Integer hDLC) {
        this.hDLC = hDLC;
    }

    public Integer getLDLC() {
        return lDLC;
    }

    public void setLDLC(Integer lDLC) {
        this.lDLC = lDLC;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

}