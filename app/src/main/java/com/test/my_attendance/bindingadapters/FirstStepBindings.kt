package com.test.my_attendance.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.test.my_attendance.R
import com.test.my_attendance.viewmodels.FirstStepViewModel
import com.test.my_attendance.viewmodels.SharedViewModel

@BindingAdapter("updateGeofenceName", "enableNextButton", requireAll = true)
    fun TextInputEditText.onTextChanged(
        sharedViewModel: SharedViewModel,
        firstStepViewModel: FirstStepViewModel
    ) {
        this.setText(sharedViewModel.geoName)
        Log.d("Bindings", sharedViewModel.geoName)
        this.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                firstStepViewModel.enableNextButton(false)
            } else {
                firstStepViewModel.enableNextButton(true)
            }
            sharedViewModel.geoName = text.toString()
            Log.d("Bindings", sharedViewModel.geoName)
        }
    }

@BindingAdapter("nextButtonEnabled", "saveGeofenceId", requireAll = true)
fun TextView.step1NextClicked(nextButtonEnabled: Boolean, sharedViewModel: SharedViewModel) {
    this.setOnClickListener {
        if (nextButtonEnabled) {
            sharedViewModel.geoId = System.currentTimeMillis()
            this.findNavController().navigate(R.id.action_firstStepFragment_to_secondStepFragment)
        }
    }
}

@BindingAdapter("setProgressVisibility")
fun ProgressBar.setProgressVisibility(nextButtonEnabled: Boolean) {
    if (nextButtonEnabled) {
        this.visibility = View.GONE
    } else {
        this.visibility = View.VISIBLE
    }
}
