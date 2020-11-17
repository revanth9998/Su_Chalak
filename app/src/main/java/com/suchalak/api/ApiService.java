package com.suchalak.api;


import com.suchalak.ResponseData;
import com.suchalak.model.DriversLoc;
import com.suchalak.model.EditProfilePojo;
import com.suchalak.model.GetAllPickupsPojo;
import com.suchalak.model.UserPickupsPojo;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiService {

    @GET("DriverApp/user_registration.php")
    Call<ResponseData> userRegistration(
            @Query("name") String name,
            @Query("email") String email,
            @Query("phonenumber") String phonenumber,
            @Query("pwd") String pwd);


    @GET("/DriverApp/user_login.php?")
    Call<ResponseData> userLogin(
            @Query("uname") String uname,
            @Query("pwd") String pwd,
            @Query("role") String role
    );


    @GET("/DriverApp/forgotpassword.php")
    Call<ResponseData> forgotpassword(@Query("emailid") String emailid);

    @GET("/DriverApp/update_pickup_status.php?")
    Call<ResponseData> updatePickupStatus(@Query("status") String status,@Query("id") String id);

    @Multipart
    @POST("/DriverApp/user_registration.php")
    Call<ResponseData> userRegistration(
            @Part MultipartBody.Part file,
            @PartMap Map<String, String> partMap

    );

    @Multipart
    @POST("/DriverApp/user_update_profile.php?")
    Call<ResponseData> user_update_profile(
            @Part MultipartBody.Part file,
            @PartMap Map<String, String> partMap

    );

    @GET("/DriverApp/get_user_profile.php?")
    Call<List<EditProfilePojo>> get_user_profile(
            @Query("uname") String uname
    );

    @GET("/DriverApp/user_pickup.php?")
    Call<ResponseData> user_pickup(
            @Query("from") String from,
            @Query("to") String to,
            @Query("uname") String uname,
            @Query("driver_uname") String driver_uname,
            @Query("user_lat") String user_lat,
            @Query("user_lng") String user_lng
    );

    @GET("/DriverApp/getallpickups.php")
    Call<List<GetAllPickupsPojo>> getallpickups(@Query("duname") String duname);

    @GET("/DriverApp/getUserPickupDetails.php")
    Call<List<UserPickupsPojo>> getUserPickupDetails(@Query("uname") String uname, @Query("status") String status);

    @GET("/DriverApp/getDriverPickupDetails.php")
    Call<List<UserPickupsPojo>> getDriverPickupDetails(@Query("uname") String uname, @Query("status") String status);

    @GET("/DriverApp/getDriverDetails.php")
    Call<List<DriversLoc>> getDriverDetails(@Query("my_lat") String my_lat,@Query("my_lng") String my_lng);

    @GET("/DriverApp/delete_driver_picups.php?")
    Call<ResponseData> deletepicup(
            @Query("id") String id

    );


}
