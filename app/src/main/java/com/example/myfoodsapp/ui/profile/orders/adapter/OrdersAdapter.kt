package com.example.myfoodsapp.ui.profile.orders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfoodsapp.databinding.ItemOrdersBinding
import com.example.myfoodsapp.room.entity.OrderItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<OrderItem>() {
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtilCallback)

    fun submitList(newList: List<OrderItem>) {
        differ.submitList(newList)
    }

    inner class OrderViewHolder(private val binding: ItemOrdersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: OrderItem) {

            Glide.with(itemView.context)
                .load("http://kasimadalan.pe.hu/yemekler/resimler/${order.image}")
                .into(binding.imgOrderPhoto)

            binding.txtOrderName.text = order.name
            binding.txtOrderTime.text = formatOrderDate(order.orderTime)
        }
    }

    fun formatOrderDate(date: Date): String {
        val sdf = SimpleDateFormat("MMMM dd, yyyy • HH:mm", Locale.getDefault())
        return sdf.format(date)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }


}