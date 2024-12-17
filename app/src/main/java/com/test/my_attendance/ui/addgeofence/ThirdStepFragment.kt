package com.test.my_attendance.ui.addgeofence

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.test.my_attendance.R
import com.test.my_attendance.databinding.FragmentThirdStepBinding
import com.test.my_attendance.viewmodels.SharedViewModel

class ThirdStepFragment : Fragment() {

    private var _binding: FragmentThirdStepBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdStepBinding.inflate(inflater, container, false)
        binding.sharedViewModel = sharedViewModel

        binding.step3Back.setOnClickListener {
            findNavController().navigate(R.id.action_thirdStepFragment_to_secondStepFragment)
        }

        binding.step3Done.setOnClickListener {
            sharedViewModel.geoRadius = binding.slider.value
            sharedViewModel.geofenceReady = true
            findNavController().navigate(R.id.action_thirdStepFragment_to_mapsFragment)
            Log.d("Step3Fragment", sharedViewModel.geoRadius.toString())
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}