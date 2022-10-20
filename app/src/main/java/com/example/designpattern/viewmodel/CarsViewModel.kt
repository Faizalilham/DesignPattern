package com.example.designpattern.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.designpattern.api.ApiService
import com.example.designpattern.model.Cars
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarsViewModel : ViewModel() {

    private var postData : MutableLiveData<Cars> = MutableLiveData()
    fun postLiveData() : LiveData<Cars> = postData

    private var updateData : MutableLiveData<Cars> = MutableLiveData()
    fun updateLiveData() : LiveData<Cars> = updateData

    private var deleteData : MutableLiveData<Cars> = MutableLiveData()
    fun deleteLiveData() : LiveData<Cars> = deleteData

    fun postData(name:RequestBody,category : RequestBody,price : RequestBody,status : RequestBody, image: MultipartBody.Part){
        ApiService.APIEndPoint().postData(name,category,price,status,image)
            .enqueue(object : Callback<Cars>{
                override fun onResponse(call: Call<Cars>, response: Response<Cars>) {
                    if(response.isSuccessful){
                        val body = response.body()
                        if(body != null){
                            postData.postValue(body)
                        }else{
                            postData.postValue(null)
                            Log.d("NULL","BODY NULL")
                        }
                    }else{
                        postData.postValue(null)
                        Log.d("NULLS","${response.code()} -> ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Cars>, t: Throwable) {
                    postData.postValue(null)
                    Log.d("NULLSES", t.message.toString())
                }

            })
    }



    fun updateDatasConfig(id :Int,name :String,category:String,price :Int,status :Boolean){
        ApiService.APIEndPoint().updateData(id,
            Cars(id,name,category,price,status,null,"","","",""))
            .enqueue(object : Callback<Cars>{
                override fun onResponse(call: Call<Cars>, response: Response<Cars>) {
                    if(response.isSuccessful){
                        val body = response.body()
                        if(body != null){
                            updateData.postValue(body)
                        }else{
                            updateData.postValue(null)
                        }
                    }else{
                        updateData.postValue(null)
                    }
                }

                override fun onFailure(call: Call<Cars>, t: Throwable) {
                   updateData.postValue(null)
                }

            })

    }

    fun deleteDataConfig(id : Int){
        ApiService.APIEndPoint().deleteData(id)
            .enqueue(object : Callback<Cars>{
                override fun onResponse(call: Call<Cars>, response: Response<Cars>) {
                    if(response.isSuccessful){
                        val body = response.body()
                        if(body != null){
                            deleteData.postValue(body)
                        }else{
                            deleteData.postValue(null)
                        }
                    }else{
                        deleteData.postValue(null)
                    }
                }

                override fun onFailure(call: Call<Cars>, t: Throwable) {
                   deleteData.postValue(null)
                }

            })
    }
}