package com.example.myfoodsapp.retrofit.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Basket(
    @SerializedName("sepet_yemek_id") val basketFoodId: String,
    @SerializedName("yemek_adi") val basketFoodName: String,
    @SerializedName("yemek_resim_adi") val basketFoodPhoto: String,
    @SerializedName("yemek_fiyat") val basketFoodPrice: String,
    @SerializedName("yemek_siparis_adet") val basketOrderQuantity: String,
    @SerializedName("kullanici_adi") val basketUsername: String
) : Parcelable

@Parcelize
data class BasketResponse(
    @SerializedName("sepet_yemekler") val basketList: MutableList<Basket>,
    @SerializedName("success") val success: Int
) : Parcelable