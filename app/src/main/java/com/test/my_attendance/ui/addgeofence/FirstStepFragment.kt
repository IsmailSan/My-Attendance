package com.test.my_attendance.ui.addgeofence

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.test.my_attendance.R
import com.test.my_attendance.databinding.FragmentFirstStepBinding
import com.test.my_attendance.viewmodels.FirstStepViewModel
import com.test.my_attendance.viewmodels.SharedViewModel
import kotlinx.coroutines.launch
import java.io.IOException


class FirstStepFragment : Fragment() {

    private var _binding : FragmentFirstStepBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val firstStepViewModel: FirstStepViewModel by viewModels()

    private lateinit var geoCoder: Geocoder
    private lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(requireContext(), getString(R.string.google_android_map_api_key))
        placesClient = Places.createClient(requireContext())
        geoCoder = Geocoder(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstStepBinding.inflate(layoutInflater, container, false)
        binding.sharedViewModel = sharedViewModel
        binding.firstStepViewModel = firstStepViewModel
        binding.lifecycleOwner = this

        binding.step1Back.setOnClickListener {
            onFirstStepBackClicked()
        }

        getCountryCodeFromCurrentLocation()

        return binding.root
    }

    private fun onFirstStepBackClicked() {
        findNavController().navigate(R.id.action_firstStepFragment_to_mapsFragment)
    }

    @SuppressLint("MissingPermission")
    private fun getCountryCodeFromCurrentLocation() {
        lifecycleScope.launch {
            val placeFields = listOf(Place.Field.LAT_LNG)
            val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

            val placeResponse = placesClient.findCurrentPlace(request)
            placeResponse.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    try {
                        val response = task.result
                        val latLng = response.placeLikelihoods.first().place.latLng!!
                        val address = geoCoder.getFromLocation(
                            latLng.latitude,
                            latLng.longitude,
                            1
                        )
                        sharedViewModel.geoCountryCode = address?.first()?.countryCode ?: ""
                    } catch (exception: IOException) {
                        Log.e("FirstStepFragment", "getFromLocation FAILED")
                    } finally {
                        enableNextButton()
                    }
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        Log.e("FirstStepFragment", exception.statusCode.toString())
                        Log.e("FirstStepFragment", exception.message.toString())
                        Log.e("FirstStepFragment", exception.cause?.stackTrace.toString())
                    }
                    enableNextButton()
                }
            }
        }
    }

    private fun enableNextButton() {
        if (sharedViewModel.geoName.isNotEmpty()) {
            firstStepViewModel.enableNextButton(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}