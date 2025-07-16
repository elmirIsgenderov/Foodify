package com.example.myfoodsapp.ui.profile.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodsapp.databinding.ItemProfileBinding
import com.example.myfoodsapp.retrofit.model.ProfileOption

class ProfileAdapter(
    private val onItemClick: (ProfileOption) -> Unit
) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<ProfileOption>() {
        override fun areItemsTheSame(oldItem: ProfileOption, newItem: ProfileOption): Boolean {
            return oldItem.type == newItem.type
        }

        override fun areContentsTheSame(oldItem: ProfileOption, newItem: ProfileOption): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, diffUtilCallback)

    fun submitList(newList: List<ProfileOption>) {
        differ.submitList(newList)
    }

    inner class ProfileViewHolder(val view: ItemProfileBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(profileOption: ProfileOption) {
            view.imgicon.setImageResource(profileOption.icon)
            view.txtTitle.text = view.root.context.getString(profileOption.titleRes)//profileOption.context.getString(profileOption.titleRes)
            itemView.setOnClickListener { onItemClick(profileOption) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size
}