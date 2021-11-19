package com.example.vinsearcher.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    val likedButtonState = MutableLiveData(false)
}