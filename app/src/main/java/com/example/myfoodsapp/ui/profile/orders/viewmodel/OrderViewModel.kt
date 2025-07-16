package com.example.myfoodsapp.ui.profile.orders.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfoodsapp.room.entity.OrderItem
import com.example.myfoodsapp.retrofit.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _orders = MutableLiveData<List<OrderItem>>()
    val orders: LiveData<List<OrderItem>>
        get() = _orders

    fun getOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getOrders()
            _orders.postValue(result)
        }
    }
}