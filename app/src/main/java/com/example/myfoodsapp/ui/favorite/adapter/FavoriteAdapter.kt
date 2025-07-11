package com.example.myfoodsapp.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfoodsapp.databinding.ItemFavoriteBinding
import com.example.myfoodsapp.retrofit.model.Food

class FavoriteAdapter(
    private val itemClick: (food: Food) -> Unit,
    private val onDeleteClick: (food: Food) -> Unit
) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

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

    inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {
            val url = "http://kasimadalan.pe.hu/yemekler/resimler/${food.foodPhotoName}"

            Glide.with(binding.imgFavorite.context)
                .load(url)
                .into(binding.imgFavorite)

            binding.txtNameFavorite.text = food.foodName
            binding.txtPriceFavorite.text = "${food.foodPrice}"


            binding.root.setOnClickListener {
                itemClick(food)
            }
            binding.imgDetele.setOnClickListener {
                onDeleteClick(food)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(differ.currentList[position])

    }

    override fun getItemCount(): Int = differ.currentList.size

}