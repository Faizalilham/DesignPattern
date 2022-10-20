package com.example.designpattern.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.designpattern.databinding.ActivityDetailBinding
import com.example.designpattern.model.Cars
import com.example.designpattern.presenter.CarsContractDetail
import com.example.designpattern.presenter.CarsPresenter

class DetailActivity : AppCompatActivity(),CarsPresenter.CarsPresenters {
    private lateinit var cars : CarsContractDetail
    private var _binding : ActivityDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cars = CarsContractDetail(this)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent.getIntExtra("id",0)
        cars.getDataById(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSuccessCars(message: String, data: Cars) {
        binding.apply {
            tvName.text = data.name
            Glide.with(binding.root).load(data.image).centerCrop().into(imageCar)
        }

    }

    override fun onFailureCars(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}