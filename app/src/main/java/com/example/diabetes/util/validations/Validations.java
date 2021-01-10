package com.example.diabetes.util.validations;

import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Validations {
    public static boolean isPassAndConfirmPass(String pass,String confirmPass){
        if(isPassword(pass) && pass.equals(confirmPass)){
            return true;
        }
        return false;
    }
    public static boolean isLogin(String email , String pass) {
        if(isEmail(email) && isPassword(pass)){
            return  true;
        }
        return false;
    }

    public static boolean isRegister(String fullname, String age, String dayOfBirth, String email, String pass) {
        if (isFullName(fullname) && isAge(age) && isDateOfBirth(dayOfBirth) && isEmail(email) && isPassword(pass)) {
            return true;
        }
        return false;
    }

    public static boolean isHeightAndWeight(String valuate) {
        try {
            Float.parseFloat(valuate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isFullName(String valuate) {
        if (valuate.equals("")) {
            return false;
        }
        return true;
    }

    public static boolean isAge(String valuate) {
        try {
            if (Integer.parseInt(valuate) >= 100 || Integer.parseInt(valuate) < 1 || valuate == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDateOfBirth(String valuate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            Date toDay = format.parse(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH)
                    + "-" + calendar.get(Calendar.DATE));
            Date day = format.parse(valuate);
            if (toDay.compareTo(day) > 0) {
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    public static boolean isPassword(String value) {
        if (value.equals("") || value.length() <= 6) {
            return false;
        }
        return true;
    }

    public static boolean isEmail(String value) {
        if (value.equals("") || !value.endsWith("@gmail.com")) {
            return false;
        }
        return true;
    }
}
