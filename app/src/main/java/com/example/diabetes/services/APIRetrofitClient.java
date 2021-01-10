package com.example.diabetes.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRetrofitClient {
    private static Retrofit retrofit=null;
    public static Retrofit getClient(String baseurl){
        OkHttpClient okHttpClient=new OkHttpClient.Builder()//những thao tác những giao thức tương tác với mạng
                                    .readTimeout(10000, TimeUnit.MILLISECONDS)//thời gian ngắt đọc phía sever
                                    .writeTimeout(10000, TimeUnit.MILLISECONDS)//thời gian ngắt
                                    .connectTimeout(10000, TimeUnit.MILLISECONDS)//thời gian kết nối
                                    .retryOnConnectionFailure(true)//tự động kết nối lại nếu lỗi mạng
                                    .protocols(Arrays.asList(Protocol.HTTP_1_1))//giao thức kết nối
                                    .build();
        Gson gson=new GsonBuilder().setLenient().create();//cấu hình json. json dùng để convert từ khóa json api trả về thành biến json của java

        retrofit=new Retrofit.Builder()
                .baseUrl(baseurl)
                .client(okHttpClient)//cấu hình những phương thức của mạng
                .addConverterFactory(GsonConverterFactory.create(gson))//convert dữ liệu của api thành biến của java
                .build();
        return retrofit;
    }
}
