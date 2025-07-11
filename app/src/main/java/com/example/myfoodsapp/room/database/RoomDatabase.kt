package com.example.myfoodsapp.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myfoodsapp.room.converter.Converters
import com.example.myfoodsapp.room.dao.CardDao
import com.example.myfoodsapp.room.dao.OrderDao
import com.example.myfoodsapp.room.entity.CardEntity
import com.example.myfoodsapp.room.entity.OrderItem

@Database(entities = [OrderItem::class, CardEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun cardDao(): CardDao
}