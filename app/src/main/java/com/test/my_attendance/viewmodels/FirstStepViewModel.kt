package com.test.my_attendance.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FirstStepViewModel : ViewModel() {

    private val _nextButtonEnabled = MutableLiveData(false)
    val nextButtonEnabled: LiveData<Boolean> get() = _nextButtonEnabled

    fun enableNextButton(enable: Boolean){
        _nextButtonEnabled.value = enable
    }

}