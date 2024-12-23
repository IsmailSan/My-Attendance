package com.test.my_attendance.bindingadapters

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.BindingAdapter
import com.test.my_attendance.R
import coil.load
import com.test.my_attendance.data.GeofenceEntity
import com.test.my_attendance.util.ExtensionFunction.disable
import com.test.my_attendance.util.ExtensionFunction.enable

@BindingAdapter("setVisibility")
fun View.setVisibility(data: List<GeofenceEntity>) {
    if (data.isNullOrEmpty()) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

@BindingAdapter("handleMotionTransition")
fun MotionLayout.handleMotionTransition(deleteImageView: ImageView) {
    deleteImageView.disable()
    this.setTransitionListener(object : MotionLayout.TransitionListener {
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        override fun onTransitionCompleted(motionLayout: MotionLayout?, transition: Int) {
            if (motionLayout != null && transition == R.id.start) {
                deleteImageView.disable()
            } else if (motionLayout != null && transition == R.id.end) {
                deleteImageView.enable()
            }
        }
    })
}

@BindingAdapter("loadImage")
fun ImageView.loadImage(bitmap: Bitmap) {
    this.load(bitmap)
}

@BindingAdapter("parseCoordinates")
fun TextView.parseCoordinates(value: Double) {
    val coordinate = String.format("%.4f", value)
    this.text = coordinate
}