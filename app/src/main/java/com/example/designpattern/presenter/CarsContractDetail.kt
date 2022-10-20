package com.example.designpattern.presenter

import com.example.designpattern.api.ApiService
import com.example.designpattern.model.Cars
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarsContractDetail(private val cars : CarsPresenter.CarsPresenters) {

    fun getDataById(id :Int){
        ApiService.APIEndPoint().getDataById(id)
            .enqueue(object : Callback<Cars> {
                override fun onResponse(call: Call<Cars>, response: Response<Cars>) {
                    if(response.isSuccessful){
                        val body = response.body()
                        if(body != null){
                            cars.onSuccessCars(response.message(), response.body()!!)
                        }
                    }
                }

                override fun onFailure(call: Call<Cars>, t: Throwable) {
                    t.message?.let { cars.onFailureCars(it) }
                }

            })
    }

}