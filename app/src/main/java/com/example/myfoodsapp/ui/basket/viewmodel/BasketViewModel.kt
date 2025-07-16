package com.example.myfoodsapp.ui.basket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfoodsapp.utils.Resource
import com.example.myfoodsapp.room.entity.OrderItem
import com.example.myfoodsapp.retrofit.repository.Repository
import com.example.myfoodsapp.retrofit.model.Basket
import com.example.myfoodsapp.retrofit.model.CrudResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {


    private val _basketFood = MutableLiveData<Resource<List<Basket>>>()
    val basketFood: LiveData<Resource<List<Basket>>>
        get() = _basketFood

    private val _clearBasket = MutableLiveData<Resource<CrudResponse>>()
    val clearBasket: LiveData<Resource<CrudResponse>>
        get() = _clearBasket

    private val _deleteFood = MutableLiveData<Resource<CrudResponse>>()
    val deleteFood: LiveData<Resource<CrudResponse>>
        get() = _deleteFood

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int>
        get() = _totalPrice


    fun getBasket(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getBasket(userName)
            _basketFood.postValue(result)
        }
    }
    fun deleteFoods(foodId: Int, username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.deleteFood(foodId, username)
            _deleteFood.postValue(result)
        }
    }

    fun totalPrice(basketList: List<Basket>) {
        val total = basketList.sumOf { it.basketFoodPrice.toInt() * it.basketOrderQuantity.toInt() }
        _totalPrice.value = total
    }

    fun insertOrder(orderItem: OrderItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertOrder(orderItem)
        }
    }

    fun clearBasket(items: List<Basket>, username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            items.forEach { item ->
                val result = repository.deleteBasketItem(item.basketFoodId.toInt(), username)
                _clearBasket.postValue(result)
            }
        }
    }
}

