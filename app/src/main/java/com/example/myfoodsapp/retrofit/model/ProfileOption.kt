package com.example.myfoodsapp.retrofit.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ProfileOption(
    @DrawableRes val icon: Int,
    @StringRes val titleRes: Int,
    val type: ProfileOptionType
)

enum class ProfileOptionType {
    PROFILE,
    PAYMENT_METHODS,
    ADDRESS,
    FAVORITE,
    ORDERS,
    LANGUAGE,
    SETTINGS,
    NOTIFICATIONS,
    LOGOUT
}
