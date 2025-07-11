package com.example.myfoodsapp.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cardNumber: String,
    val cardHolder: String,
    val expiryDate: String,
    val cvv: String
)