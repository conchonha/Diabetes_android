package com.example.diabetes.services;

import com.example.diabetes.model.UserReponse.UserReponse;
import com.example.diabetes.model.*;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DataService {
    @FormUrlEncoded
    @POST("rest-api/User/register")
    Call<UserReponse>register(@Field("fullname") String fullname,
                              @Field("email") String email,
                              @Field("password") String password,
                              @Field("age") String age,
                              @Field("dayofbirth") String dayofbirth,
                              @Field("sex") String sex,
                              @Field("check") int check);

    @FormUrlEncoded
    @POST("rest-api/User/login")
    Call<UserReponse>login(@Field("email") String email,
                    @Field("password") String pass);

    @FormUrlEncoded
    @POST("rest-api/User/updatePass")
    Call<String>updatePass(@Field("email") String email,
                    @Field("password") String pass);

    @FormUrlEncoded
    @POST("rest-api/Menu/postFood")
    Call<String>postFood(@Field("id_mold") int id_mold,
                           @Field("id_user") int id_user,
                           @Field("namefood") String namefood,
                           @Field("image") String image,
                           @Field("gam") int gam,
                           @Field("day") String day,
                           @Field("check") int check,
                            @Field("id_menu") int idmenu
                           );

    @GET("rest-api/Menu/getListFood")
    Call<ArrayList<Menu>>getListFood(@Query("id_mold") int id_mold,
                                     @Query("id_user") int id_user);

    @GET("rest-api/Menu/deleteFood")
    Call<String>deleteFood(@Query("id_Menu") int id_Menu);

    @GET("rest-api/Menu/getDataFoodOffer")
    Call<ArrayList<Menu>>getDataFoodOffer();

    @FormUrlEncoded
    @POST("rest-api/HealthInformation/insert")
    Call<HealthInformationModel>insert(@Field("heights") String heights,
                                       @Field("weights") String weights,
                                       @Field("bloodPressure") String bloodPressure,
                                       @Field("bloodSugar") String bloodSugar,
                                       @Field("CPR") String CPR,
                                       @Field("HDL_C") String HDL_C,
                                       @Field("LDL_C") String LDL_C,
                                       @Field("time") String time,
                                       @Field("id_user") String id_user,
                                       @Field("id") int check);

    @GET("rest-api/HealthInformation/getLastHealthInformation")
    Call<HealthInformationModel>getLastHealthInformation();

    @FormUrlEncoded
    @POST("rest-api/HealthInformation/getObjectHealthInformation")
    Call<HealthInformationModel>getObjectHealthInformation(@Field("time") String time);

    @FormUrlEncoded
    @POST("rest-api/HealthInformation/deleteObject")
    Call<String>deleteObject(@Field("date") String date);

    @GET("rest-api/HealthInformation/getBloodPressureChart")
    Call<List<HealthInformationModel>>getBloodPressureChart();
}
