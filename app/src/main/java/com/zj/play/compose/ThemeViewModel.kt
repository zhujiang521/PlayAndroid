package com.zj.play.compose

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {

    private val _theme = MutableLiveData(false)
    val theme: LiveData<Boolean> = _theme

    fun onThemeChanged(position: Boolean) {
        _theme.value = position
    }

}