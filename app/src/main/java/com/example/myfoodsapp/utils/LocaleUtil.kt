package com.example.myfoodsapp.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleUtil {
    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}