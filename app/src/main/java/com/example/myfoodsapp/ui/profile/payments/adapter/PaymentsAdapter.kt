package com.example.myfoodsapp.ui.profile.payments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodsapp.databinding.ItemPaymentsBinding
import com.example.myfoodsapp.retrofit.model.PaymentItem

class PaymentsAdapter : RecyclerView.Adapter<PaymentsAdapter.PaymentsViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<PaymentItem>() {
        override fun areItemsTheSame(oldItem: PaymentItem, newItem: PaymentItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PaymentItem, newItem: PaymentItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtilCallback)

    fun submitList(newList: List<PaymentItem>) {
        differ.submitList(newList)
    }


    inner class PaymentsViewHolder(private val binding: ItemPaymentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(payments: PaymentItem) {
            binding.imgPayments.setImageResource(payments.iconResId)
            binding.txtTitlePayments.text = payments.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentsViewHolder {
        val binding =
            ItemPaymentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size
}
