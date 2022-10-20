package com.example.designpattern

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.designpattern.adapter.CarsAdapter
import com.example.designpattern.databinding.ActivityMainBinding
import com.example.designpattern.model.Cars
import com.example.designpattern.presenter.CarsContractPresenter
import com.example.designpattern.presenter.CarsPresenter
import com.example.designpattern.view.DetailActivity
import com.example.designpattern.view.PostActivity
import com.example.designpattern.viewmodel.CarsViewModel

class MainActivity : AppCompatActivity(),CarsPresenter {
    private lateinit var cars : CarsContractPresenter
    private lateinit var carsAdapter : CarsAdapter
    private lateinit var carsViewModel : CarsViewModel
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cars = CarsContractPresenter(this)
        carsViewModel = ViewModelProvider(this)[CarsViewModel::class.java]
        _binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecycler()
        doPost()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun doPost(){
        binding.fab.setOnClickListener {
            startActivity(Intent(this,PostActivity::class.java).also {
                it.putExtra("id",0)
            })
        }
    }

    override fun onSuccess(message: String, data: MutableList<Cars>) {
        carsAdapter.submitData(data)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    private fun setRecycler(){
        carsAdapter = CarsAdapter(object : CarsAdapter.Clicked{
            override fun onClick(cars: Cars) {
               startActivity(Intent(this@MainActivity,DetailActivity::class.java).also{
                   it.putExtra("id",cars.id)
               })
            }

            override fun onUpdate(cars: Cars) {
                startActivity(Intent(this@MainActivity, PostActivity::class.java).also{
                    it.putExtra("id",cars.id)
                })
            }

            override fun ondelete(cars: Cars) {
                carsViewModel.deleteDataConfig(cars.id)
                carsViewModel.deleteLiveData().observe(this@MainActivity){
                    if(it != null){
                        Toast.makeText(this@MainActivity, "Delete data success", Toast.LENGTH_SHORT).show()
                    }
                }

                this@MainActivity.cars.getAllCars()
            }

        })
        cars.getAllCars()
        binding.newsRecycler.apply {
            adapter = carsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}