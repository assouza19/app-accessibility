package com.example.goout.ViewModel

import android.text.Editable
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private var _fieldsPresent = MutableLiveData<Int>()

    val fieldsPresent: LiveData<Int>
        get() = _fieldsPresent

    fun init() {
        _fieldsPresent.value = View.VISIBLE
    }

    fun validateFields(username: Editable?, password: Editable?): Boolean {
        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            return false
        }
        return true
    }
}