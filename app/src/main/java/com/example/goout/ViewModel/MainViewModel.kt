package com.example.goout.ViewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private var _shimmerPresent = MutableLiveData<Int>()

    val shimmerPresent: MutableLiveData<Int>
        get() = _shimmerPresent

    fun init() {
      _shimmerPresent.value = View.GONE
    }
}