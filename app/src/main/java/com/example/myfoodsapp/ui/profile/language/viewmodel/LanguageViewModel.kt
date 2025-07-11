package com.example.myfoodsapp.ui.profile.language.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfoodsapp.retrofit.repository.Repository
import com.example.myfoodsapp.retrofit.model.LanguageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _languageList = MutableLiveData<List<LanguageItem>>()
    val languageList: LiveData<List<LanguageItem>>
        get() = _languageList

    private val _currentLanguage = MutableLiveData<String>()
    val currentLanguage: LiveData<String>
        get() = _currentLanguage

    init {
        loadLanguages()
        loadCurrentLanguage()
    }

    private fun loadLanguages() {
        _languageList.value = repository.getLanguageList()
    }

    private fun loadCurrentLanguage() {
        _currentLanguage.value = repository.getCurrentLanguage()
    }

    fun onLanguageSelected(newLang: String) {
        repository.saveLanguage(newLang)
        _currentLanguage.value = newLang
    }
}