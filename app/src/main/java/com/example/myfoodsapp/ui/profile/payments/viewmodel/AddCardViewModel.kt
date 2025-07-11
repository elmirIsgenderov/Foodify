package com.example.myfoodsapp.ui.profile.payments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfoodsapp.room.entity.CardEntity
import com.example.myfoodsapp.retrofit.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _insertResult = MutableLiveData<Boolean>()
    val insertResult: LiveData<Boolean>
        get() = _insertResult

    fun insertCard(cardEntity: CardEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertCard(cardEntity)
                _insertResult.postValue(true)
            } catch (e: Exception) {
                _insertResult.postValue(false)
            }
        }
    }
}