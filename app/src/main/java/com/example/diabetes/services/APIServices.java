package com.example.diabetes.services;

public class APIServices {
    private static String ip = "http://192.168.1.37";
    private static String baseurl="http://192.168.1.6/Diabetes_Laravel/public/";
   // private static String baseurl = ip + "/diabetes_laravel/public/";
    //public static String urlImage = ip + "/diabetes_laravel/public/";
    public static String urlImage="http://192.168.1.6/Diabetes_Laravel/public/";

    public static DataService getService() {
        return APIRetrofitClient.getClient(baseurl).create(DataService.class);
    }
}
