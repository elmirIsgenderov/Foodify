package com.example.myfoodsapp.ui.profile.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfoodsapp.retrofit.repository.Repository
import com.example.myfoodsapp.retrofit.model.ProfileOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _profileOption = MutableLiveData<List<ProfileOption>>()
    val profileOption: LiveData<List<ProfileOption>>
        get() = _profileOption

    private val _logoutSuccess = MutableLiveData<Unit>()
    val logoutSuccess: LiveData<Unit>
        get() = _logoutSuccess


    init {
        loadOptions()
    }

    private fun loadOptions() {
        viewModelScope.launch(Dispatchers.IO) {
            _profileOption.postValue(repository.getProfileOptions())
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.logout()
                _logoutSuccess.postValue((Unit))
            } catch (e: Exception) {
                Log.e("ProfileVM", "Logout error: ${e.message}")
            }
        }
    }
}