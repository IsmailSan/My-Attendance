package com.test.my_attendance.ui.geofences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.my_attendance.R
import com.test.my_attendance.adapters.GeofenceAdapter
import com.test.my_attendance.databinding.FragmentGeofencesBinding
import com.test.my_attendance.viewmodels.SharedViewModel


class GeofencesFragment : Fragment() {

    private var _binding: FragmentGeofencesBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val geofencesAdapter by lazy { GeofenceAdapter(sharedViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeofencesBinding.inflate(inflater, container, false)
        binding.sharedViewModel = sharedViewModel

        setupToolbar()
        setupRecyclerView()
        observeDatabase()

        return binding.root
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        binding.geofencesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.geofencesRecyclerView.adapter = geofencesAdapter
    }

    private fun observeDatabase() {
        sharedViewModel.readGeofences.observe(viewLifecycleOwner) {
            geofencesAdapter.setData(it)
            binding.geofencesRecyclerView.scheduleLayoutAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}