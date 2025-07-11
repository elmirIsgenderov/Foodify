package com.example.myfoodsapp.retrofit.datasource

import android.content.Context
import com.example.myfoodsapp.R
import com.example.myfoodsapp.room.dao.CardDao
import com.example.myfoodsapp.room.dao.OrderDao
import com.example.myfoodsapp.room.entity.CardEntity
import com.example.myfoodsapp.room.entity.OrderItem
import com.example.myfoodsapp.retrofit.model.LanguageItem
import com.example.myfoodsapp.retrofit.model.ProfileOption
import com.example.myfoodsapp.retrofit.model.ProfileOptionType

class LocalDataSource(
    context: Context,
    private val orderDao: OrderDao,
    private val cardDao: CardDao
) {

    private val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    // Order
    suspend fun insertOrder(orderItem: OrderItem) = orderDao.insertOrder(orderItem)
    suspend fun getOrders() = orderDao.getOrders()

    // Card
    suspend fun insertCard(cardEntity: CardEntity) = cardDao.insertCard(cardEntity)
    suspend fun getAllCards() = cardDao.getAllCards()

    //Language
    fun getCurrentLanguage(): String {
        return prefs.getString("lang", "en") ?: "en"
    }

    fun saveLanguage(code: String) {
        prefs.edit().putString("lang", code).apply()
    }

    fun getLanguageList(): List<LanguageItem> {
        return listOf(
            LanguageItem("en", "English"),
            LanguageItem("tr", "Turkish")
        )
    }

    fun getProfileOptions(): List<ProfileOption> {

        return listOf(
            ProfileOption(R.drawable.ic_user, R.string.your_profile, ProfileOptionType.PROFILE),
            ProfileOption(
                R.drawable.ic_payment,
                R.string.payment_methods,
                ProfileOptionType.PAYMENT_METHODS
            ),
            ProfileOption(R.drawable.ic_address, R.string.address, ProfileOptionType.ADDRESS),
            ProfileOption(R.drawable.ic_like_empty, R.string.favorite, ProfileOptionType.FAVORITE),
            ProfileOption(R.drawable.ic_order, R.string.orders, ProfileOptionType.ORDERS),
            ProfileOption(R.drawable.ic_language, R.string.language, ProfileOptionType.LANGUAGE),
            ProfileOption(R.drawable.ic_settings, R.string.setting, ProfileOptionType.SETTINGS),
            ProfileOption(
                R.drawable.ic_notifications,
                R.string.notifications,
                ProfileOptionType.NOTIFICATIONS
            ),
            ProfileOption(R.drawable.ic_logout, R.string.logout, ProfileOptionType.LOGOUT)
        )
    }

}