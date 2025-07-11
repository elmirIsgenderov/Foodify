package com.example.myfoodsapp.ui.profile.language.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myfoodsapp.databinding.ItemLanguageBinding
import com.example.myfoodsapp.retrofit.model.LanguageItem

class LanguageAdapter(
    private val onLanguageSelected: (String) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<LanguageItem>() {
        override fun areItemsTheSame(oldItem: LanguageItem, newItem: LanguageItem): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: LanguageItem, newItem: LanguageItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtilCallback)

    fun submitList(newList: List<LanguageItem>) {
        differ.submitList(newList)
    }

    private var selectedLanguageCode: String = ""

    fun setSelectedLanguage(code: String) {
        selectedLanguageCode = code
        notifyDataSetChanged()
    }

    inner class LanguageViewHolder(val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(languageItem: LanguageItem) {
            binding.txtLanguageName.text = languageItem.name
            binding.radioButton.isChecked = languageItem.code == selectedLanguageCode

            binding.radioButton.setOnClickListener {
                selectedLanguageCode = languageItem.code
                notifyDataSetChanged()
                onLanguageSelected(languageItem.code)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = ItemLanguageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size
}
