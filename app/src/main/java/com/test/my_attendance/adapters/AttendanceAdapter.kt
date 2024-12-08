//package com.kostbuddy.mobile_attendance.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.kostbuddy.mobile_attendance.databinding.ItemAttendanceBinding
//
//
//class AttendanceAdapter(
//    private val bookingHistoryList: List<>,
//) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {
//
//    lateinit var binding: ItemAttendanceBinding
//
//    inner class ViewHolder(
//        private val binding: ItemAttendanceBinding
//    ) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(
//            myBookings:
//        ) {
//            itemView.apply {
//                with(binding) {
//
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): AttendanceAdapter.ViewHolder {
//        binding =
//            ItemAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(bookingHistoryList[position])
//    }
//
//    override fun getItemCount(): Int {
//        return bookingHistoryList.size
//    }
//}