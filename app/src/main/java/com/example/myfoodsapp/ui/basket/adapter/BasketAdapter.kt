package com.example.myfoodsapp.ui.basket.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfoodsapp.databinding.ItemBasketBinding
import com.example.myfoodsapp.retrofit.model.Basket

class BasketAdapter(
    private val context: Context,
    private val onDeleteClick: (foodId: Int, username: String) -> Unit,
) :
    RecyclerView.Adapter<BasketAdapter.BasketViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<Basket>() {
        override fun areItemsTheSame(oldItem: Basket, newItem: Basket): Boolean {
            return oldItem.basketFoodId == newItem.basketFoodId
        }

        override fun areContentsTheSame(oldItem: Basket, newItem: Basket): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, diffUtilCallback)

    fun submitList(newList: List<Basket>) {
        differ.submitList(newList)
    }

    fun getCurrentItems(): List<Basket> {
        return differ.currentList
    }

    inner class BasketViewHolder(private val binding: ItemBasketBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(basket: Basket) {

            val url = "http://kasimadalan.pe.hu/yemekler/resimler/${basket.basketFoodPhoto}"

            Glide.with(binding.imgBasket.context)
                .load(url)
                .into(binding.imgBasket)

            val totalPrice = basket.basketFoodPrice.toInt() * basket.basketOrderQuantity.toInt()

            binding.foodNameBasket.text = basket.basketFoodName
            binding.txtPriceBasket.text = basket.basketFoodPrice
            binding.txtPiece.text = basket.basketOrderQuantity
            binding.txtTotal.text = "$totalPrice"


            binding.imgDeteleBasket.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Basket")
                    .setMessage("Are you sure to delete food?")
                    .setPositiveButton("Yes") { _, _ ->
                        onDeleteClick(basket.basketFoodId.toInt(), "elmir")
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val binding =
            ItemBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BasketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size
}