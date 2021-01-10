package com.example.diabetes.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailModel implements Serializable {
    private String mName;
    private int mIdMold;
    private int mIdUser;

    public DetailModel(String mName,int id,int iduser) {
        this.mName = mName;
        this.mIdMold = id;
        this.mIdUser= iduser;
    }

    public int getmIdUser() {
        return mIdUser;
    }

    public void setmIdUser(int mIdUser) {
        this.mIdUser = mIdUser;
    }

    public int getmIdMold() {
        return mIdMold;
    }

    public void setmIdMold(int mIdMold) {
        this.mIdMold = mIdMold;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

}
