package com.example.myfoodsapp.ui.profile.payments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfoodsapp.retrofit.repository.Repository
import com.example.myfoodsapp.retrofit.model.PaymentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _payments = MutableLiveData<List<PaymentItem>>()
    val payments: LiveData<List<PaymentItem>>
        get() = _payments


    fun getPayments() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getPayments()
                _payments.postValue(result)
            } catch (e: Exception) {
                _payments.postValue(emptyList())
            }
        }
    }
}