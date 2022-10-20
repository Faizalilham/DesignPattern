package com.example.designpattern.api

import com.example.designpattern.model.Cars
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiEndPoint {

    @GET("admin/car")
    fun getCar():Call<MutableList<Cars>>

    @GET("admin/car/{id}")
    fun getDataById(@Path("id") id : Int):Call<Cars>


//    @Headers("Content-Type: multipart/form-data")
    @Multipart
    @POST("admin/car")
    fun postData(@Part("name") name : RequestBody,
                 @Part("category") category : RequestBody,
                 @Part("price") price : RequestBody,
                 @Part("status") status : RequestBody,
                 @Part image : MultipartBody.Part):Call<Cars>

    @PUT("admin/car/{id}")
    fun updateData(@Path("id")id :Int,
                    @Body car : Cars):Call<Cars>

    @DELETE("admin/car/{id}")
    fun deleteData(@Path("id") id :Int):Call<Cars>
}