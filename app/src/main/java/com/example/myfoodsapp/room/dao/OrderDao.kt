package com.example.myfoodsapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myfoodsapp.room.entity.OrderItem

@Dao
interface OrderDao {

    @Query("SELECT * FROM order_table ORDER BY orderTime DESC")
    suspend fun getOrders(): List<OrderItem>

    @Insert
    suspend fun insertOrder(orderItem: OrderItem)

}