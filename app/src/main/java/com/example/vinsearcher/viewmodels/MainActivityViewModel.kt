package com.example.vinsearcher.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    val likedButtonState = MutableLiveData(false)
}