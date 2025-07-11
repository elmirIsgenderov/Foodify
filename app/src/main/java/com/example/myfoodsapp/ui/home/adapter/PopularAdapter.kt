package com.example.myfoodsapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodsapp.databinding.ItemPopularBinding
import com.example.myfoodsapp.retrofit.model.PopularRestaurants

class PopularAdapter(private var imageList: List<PopularRestaurants>) :
    RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    inner class PopularViewHolder(val view: ItemPopularBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val view = ItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val popular = imageList[position]

        holder.view.imgPopular.setImageResource(popular.image)
        holder.view.txtPopName.text = popular.restaurantName

    }

    fun updateData(newList: List<PopularRestaurants>) {
        imageList = newList
        notifyDataSetChanged()
    }

}