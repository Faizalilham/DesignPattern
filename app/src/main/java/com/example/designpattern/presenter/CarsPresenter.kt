package com.example.designpattern.presenter

import com.example.designpattern.model.Cars

interface CarsPresenter {
    fun onSuccess(message : String, data : MutableList<Cars>)
    fun onFailure(message : String)
    interface CarsPresenters{
        fun onSuccessCars(message : String, data : Cars)
        fun onFailureCars(message : String)
    }
}