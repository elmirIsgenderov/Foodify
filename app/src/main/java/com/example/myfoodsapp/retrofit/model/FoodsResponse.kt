package com.example.myfoodsapp.retrofit.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Food(
    @SerializedName("yemek_id") val foodId: Int = 0,
    @SerializedName("yemek_adi") val foodName: String = "",
    @SerializedName("yemek_resim_adi") val foodPhotoName: String = "",
    @SerializedName("yemek_fiyat") val foodPrice: Int = 0
) : Parcelable

@Parcelize
data class FoodsResponse(
    @SerializedName("yemekler") val foodList: List<Food>,
    @SerializedName("success") val success: Int
) : Parcelable