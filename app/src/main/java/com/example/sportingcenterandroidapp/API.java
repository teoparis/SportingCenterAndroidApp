package com.example.sportingcenterandroidapp;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

    @POST("register")
    Call<ResponseBody> createUser(
            @Body User user
    );

    @POST("auth/signin")
    Call<ResponseLogin> checkUser(
            @Body LoginRequest user
    );

    @GET("trainer/todayevents")
    Call<List<ResponseEvento>> todayEvents(

    );

    @GET("trainer/getusers/{id}")
    Call<List<UserTwo>> findBookedFromEventId(
            @Path("id") Integer id
    );

    @POST("trainer/setuserspresence/{id}")
    Call<ResponseBody> setUsersPresence(
            @Path("id") Integer id,
            @Body List<UserTwo> usersList);



}
