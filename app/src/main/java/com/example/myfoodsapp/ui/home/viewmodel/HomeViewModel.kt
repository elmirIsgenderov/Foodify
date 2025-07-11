package com.example.myfoodsapp.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfoodsapp.utils.Resource
import com.example.myfoodsapp.retrofit.repository.Repository
import com.example.myfoodsapp.retrofit.model.Food
import com.example.myfoodsapp.retrofit.model.PopularRestaurants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _currentFoods = MutableLiveData<Resource<List<Food>>>()
    val currentFoods: LiveData<Resource<List<Food>>>
        get() = _currentFoods

    private val _popularRestaurants = MutableLiveData<List<PopularRestaurants>>()
    val popularRestaurants: LiveData<List<PopularRestaurants>>
        get() = _popularRestaurants

    private val _searchResult = MutableLiveData<Resource<List<Food>>>()
    val searchResult: LiveData<Resource<List<Food>>>
        get() = _searchResult


    fun getCurrentFoods() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getCurrentFoods()
            _currentFoods.postValue(result)
        }
    }

    fun getSearchFood(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getSearchFood(query)
            _searchResult.postValue(result)
        }
    }

    fun getPopularRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getPopRestaurant()
                _popularRestaurants.postValue(result)
            } catch (e: Exception) {
                _popularRestaurants.postValue(emptyList())
            }
        }
    }
}
