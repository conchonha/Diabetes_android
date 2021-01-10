package com.example.diabetes;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String SHARED_PREFERENCES_NAME = "PREFERENCE_FILE_KEY";
    private static final String KEY_SAVE_TOKEN = "key_save_device_token";
    private static final String KEY_SAVE_TOKEN_USER_DEVICE = "key_save_token";

    private static final String KEY_SAVE_NAME = "key_save_name";
    private static final String KEY_SAVE_CHECK = "key_save_check";
    private static final String KEY_LOGIN = "islogin";
    private static final String KEY_INTENT = "key_intent";
    private static final String KEY_STEP = "key_step";
    private static final String KEY_STEP100 = "key_step";
    private static final String KEY_STEP1000 = "key_step";


    private static SessionManager sInstance;

    private SharedPreferences sharedPref;

    public synchronized static SessionManager getInstance() {//kiểm
        if (sInstance == null) {
            sInstance = new SessionManager();
        }
        return sInstance;
    }

    private SessionManager() {
        // no instance
    }

    public void init(Context context) {
        sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    //check chuc vu
    public void setKeyStep(int step) {
        sharedPref.edit().putInt(KEY_STEP, step).apply();
    }

    public int getKeyStep() {
        return sharedPref.getInt(KEY_STEP, 0);
    }

    //check chuc vu
    public void setKeyStep100(boolean step) {
        sharedPref.edit().putBoolean(KEY_STEP100, step).apply();
    }

    public boolean getKeyStep100() {
        return sharedPref.getBoolean(KEY_STEP100, false);
    }

    //check chuc vu
    public void setKeyStep1000(boolean step) {
        sharedPref.edit().putBoolean(KEY_STEP, step).apply();
    }

    public boolean getKeyStep1000() {
        return sharedPref.getBoolean(KEY_STEP, false);
    }

    //check login
    public void setKeyLogin(boolean islogin) {
        sharedPref.edit().putBoolean(KEY_LOGIN, islogin).apply();
    }

    //    public void getKeyLogin(boolean islogin) {
//        sharedPref.edit().putBoolean(KEY_LOGIN,islogin).apply();
//    }
    public boolean CheckKeyLogin() {
        return sharedPref.getBoolean(KEY_LOGIN, true);
    }

    //check intent
    public void setKeyIntent(boolean intent) {
        sharedPref.edit().putBoolean(KEY_INTENT, intent).apply();
    }

    public boolean getKeyIntent() {
        return sharedPref.getBoolean(KEY_INTENT, true);
    }

    /**
     * Set key save name
     */
    public void setKeySaveName(String name) {
        sharedPref.edit().putString(KEY_SAVE_NAME, name).apply();
    }

    public String getKeySaveName() {
        return sharedPref.getString(KEY_SAVE_NAME, "");
    }

    /**
     * Set key save checkin/out
     */
    public void setKeySaveCheck(Boolean check) {
        sharedPref.edit().putBoolean(KEY_SAVE_CHECK, check).apply();
    }

    public Boolean getKeySaveCheck() {
        return sharedPref.getBoolean(KEY_SAVE_CHECK, false);
    }

    /**
     * clear share
     */
    public void clear() {
        sharedPref.edit().clear().apply();
    }
}