package com.example.myfoodsapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfoodsapp.databinding.ItemRecentBinding
import com.example.myfoodsapp.retrofit.model.Food


class RecentAdapter(private val itemClick: (food: Food) -> Unit) :
    RecyclerView.Adapter<RecentAdapter.RecentViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.foodId == newItem.foodId
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtilCallback)

    fun submitList(newList: List<Food>) {
        differ.submitList(newList)
    }

    inner class RecentViewHolder(private val binding: ItemRecentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {
            val url = "http://kasimadalan.pe.hu/yemekler/resimler/${food.foodPhotoName}"

            Glide.with(binding.imgFoodPicture.context)
                .load(url)
                .into(binding.imgFoodPicture)

            binding.txtFoodName.text = food.foodName
            binding.txtFoodPrice.text = "${food.foodPrice}"

            binding.root.setOnClickListener {
                itemClick(food)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val binding = ItemRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        holder.bind(differ.currentList[position])

    }

    override fun getItemCount(): Int = differ.currentList.size
}
