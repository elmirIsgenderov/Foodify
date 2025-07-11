package com.example.myfoodsapp.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "order_table")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val image: String,
    val price: Int,
    val orderTime: Date = Date()
)
