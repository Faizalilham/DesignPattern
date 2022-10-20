package com.example.designpattern.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.AsyncListUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.designpattern.databinding.ListItemBinding
import com.example.designpattern.model.Cars

class CarsAdapter(val listener : Clicked):RecyclerView.Adapter<CarsAdapter.CarsViewHolder>() {

    val differ = object : DiffUtil.ItemCallback<Cars>(){
        override fun areItemsTheSame(oldItem: Cars, newItem: Cars): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cars, newItem: Cars): Boolean {
           return when{
               oldItem.id != newItem.id -> false
               oldItem.name != newItem.name -> false
               oldItem.category != newItem.category -> false
               oldItem.price != newItem.price -> false
               oldItem.status != newItem.status -> false
               oldItem.image != newItem.image -> false
               else -> true
           }
        }

    }
    val diffUtil = AsyncListDiffer(this,differ)

    fun submitData(data : MutableList<Cars>) = diffUtil.submitList(data)

    inner class CarsViewHolder(val binding : ListItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsViewHolder {
        return CarsViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CarsViewHolder, position: Int) {
        holder.binding.apply {
            val name = diffUtil.currentList[position].name
            tvName.text = name
            tvName.setOnClickListener {
                listener.onClick(diffUtil.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int = diffUtil.currentList.size

    interface Clicked{
        fun onClick(cars : Cars)
        fun onUpdate(cars : Cars)
        fun ondelete(cars : Cars)
    }
}