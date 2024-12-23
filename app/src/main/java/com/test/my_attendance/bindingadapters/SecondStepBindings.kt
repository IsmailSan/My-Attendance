package com.test.my_attendance.bindingadapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.test.my_attendance.util.ExtensionFunction.hide
import com.test.my_attendance.util.ExtensionFunction.show

@BindingAdapter("handleNetworkConnection", "handleRecyclerView", requireAll = true)
fun TextInputLayout.handleNetworkConnection(networkAvailable: Boolean, recyclerView: RecyclerView) {
    if (!networkAvailable) {
        this.isErrorEnabled = true
        this.error = "No Internet Connection."
        recyclerView.hide()
    } else {
        this.isErrorEnabled = false
        this.error = null
        recyclerView.show()
    }
}