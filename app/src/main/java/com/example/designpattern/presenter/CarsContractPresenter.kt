package com.example.designpattern.presenter

import com.example.designpattern.api.ApiService
import com.example.designpattern.model.Cars
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarsContractPresenter(private val cars : CarsPresenter) {

    fun getAllCars(){
        ApiService.APIEndPoint().getCar()
            .enqueue(object : Callback<MutableList<Cars>>{
                override fun onResponse(
                    call: Call<MutableList<Cars>>,
                    response: Response<MutableList<Cars>>
                ) {
                   if(response.isSuccessful){
                       val body = response.body()
                       if(body != null){
                           cars.onSuccess(response.message(), response.body()!!)
                       }
                   }
                }

                override fun onFailure(call: Call<MutableList<Cars>>, t: Throwable) {
                    t.message?.let { cars.onFailure(it) }
                }

            })
    }


}