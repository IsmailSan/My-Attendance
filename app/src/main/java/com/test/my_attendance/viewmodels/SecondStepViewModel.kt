package com.test.my_attendance.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SecondStepViewModel : ViewModel() {

    private val _nextButtonEnabled = MutableLiveData(false)
    val nextButtonEnabled: LiveData<Boolean> get() = _nextButtonEnabled

    private val _internetAvailable = MutableLiveData(true)
    val internetAvailable: LiveData<Boolean> get() = _internetAvailable

    fun enableNextButton(enable: Boolean){
        _nextButtonEnabled.value = enable
    }

    fun setInternetAvailability(online: Boolean){
        _internetAvailable.value = online
    }

}