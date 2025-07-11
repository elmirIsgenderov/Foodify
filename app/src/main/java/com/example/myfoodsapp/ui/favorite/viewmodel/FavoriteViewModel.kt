package com.example.myfoodsapp.ui.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfoodsapp.retrofit.repository.Repository
import com.example.myfoodsapp.retrofit.model.Food
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _favoriteFoods = MutableLiveData<List<Food>>()
    val favoriteFoods: LiveData<List<Food>>
        get() = _favoriteFoods

    fun fetchFavoriteFoods() {
        repository.getFavoriteFoods { foodList ->
            _favoriteFoods.value = foodList
        }
    }

    fun removeFromFavorites(foodId: Int) {
        repository.getRemoveFromFavorites(foodId)
    }


}