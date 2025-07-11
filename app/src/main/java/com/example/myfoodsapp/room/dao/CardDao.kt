package com.example.myfoodsapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myfoodsapp.room.entity.CardEntity

@Dao
interface CardDao {
    @Insert
    suspend fun insertCard(card: CardEntity)

    @Query("SELECT * FROM cards")
    suspend fun getAllCards(): List<CardEntity>
}