package com.example.myfoodsapp.retrofit.apiservice

import com.example.myfoodsapp.retrofit.model.BasketResponse
import com.example.myfoodsapp.retrofit.model.CrudResponse
import com.example.myfoodsapp.retrofit.model.FoodsResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodsApiService {

    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun getCurrentFoods(): FoodsResponse

    @POST("yemekler/sepeteYemekEkle.php")
    @FormUrlEncoded
    suspend fun addToCart(
        @Field("yemek_adi") foodName: String,
        @Field("yemek_resim_adi") foodPhotoName: String,
        @Field("yemek_fiyat") foodPrice: Int,
        @Field("yemek_siparis_adet") foodOrderQuantity: Int,
        @Field("kullanici_adi") userName: String
    ): CrudResponse

    @POST("yemekler/sepettekiYemekleriGetir.php")
    @FormUrlEncoded
    suspend fun getBasket(
        @Field("kullanici_adi") userName: String
    ): BasketResponse

    @POST("yemekler/sepettenYemekSil.php")
    @FormUrlEncoded
    suspend fun deleteFood(
        @Field("sepet_yemek_id") foodId: Int,
        @Field("kullanici_adi") userName: String
    ): CrudResponse


}