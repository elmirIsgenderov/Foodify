package com.example.myfoodsapp.ui.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfoodsapp.utils.Resource
import com.example.myfoodsapp.retrofit.repository.Repository
import com.example.myfoodsapp.retrofit.model.CrudResponse
import com.example.myfoodsapp.retrofit.model.Food
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    private val _quantity = MutableLiveData(1)
    val quantity: LiveData<Int>
        get() = _quantity

    private val _addToCart = MutableLiveData<Resource<CrudResponse>>()
    val addToCart: LiveData<Resource<CrudResponse>>
        get() = _addToCart


    fun addToCart(
        foodName: String,
        foodPhotoName: String,
        foodPrice: Int,
        foodOrderQuantity: Int,
        username: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.addToCart(
                    foodName,
                    foodPhotoName,
                    foodPrice,
                    foodOrderQuantity,
                    username
                )
                _addToCart.postValue(Resource.Success(response))
            } catch (e: Exception) {
                _addToCart.postValue(Resource.Error("Səbətə əlavə edilərkən xəta baş verdi."))
            }

        }
    }

    fun checkIfFavorite(foodId: Int) {
        repository.getCheckIfFavorite(foodId) { isFav ->
            _isFavorite.postValue(isFav)
        }
    }

    fun toggleFavorite(food: Food) {
        val current = _isFavorite.value ?: false
        repository.getToggleFavorite(food, current) { updated ->
            _isFavorite.postValue(updated)

        }
    }

    fun increaseQuantity() {
        _quantity.value = (_quantity.value ?: 1) + 1
    }

    fun decreaseQuantity() {
        val current = _quantity.value ?: 1
        if (current > 1) _quantity.value = current - 1
    }

    fun calculateTotalPrice(unitPrice: Int): Int {
        return (_quantity.value ?: 1) * unitPrice
    }

}