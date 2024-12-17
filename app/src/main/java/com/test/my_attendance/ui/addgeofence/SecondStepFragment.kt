package com.test.my_attendance.ui.addgeofence

import PredictionsAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.test.my_attendance.R
import com.test.my_attendance.databinding.FragmentSecondStepBinding
import com.test.my_attendance.util.ExtensionFunction.hide
import com.test.my_attendance.util.NetworkListener
import com.test.my_attendance.viewmodels.SecondStepViewModel
import com.test.my_attendance.viewmodels.SharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception

class SecondStepFragment : Fragment() {

    private var _binding: FragmentSecondStepBinding? = null
    private val binding get() = _binding!!

    private val predictionsAdapter by lazy { PredictionsAdapter() }

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val secondStepViewModel: SecondStepViewModel by viewModels()

    private lateinit var placesClient: PlacesClient

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(requireContext(), getString(R.string.google_android_map_api_key))
        placesClient = Places.createClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondStepBinding.inflate(inflater, container, false)
        binding.sharedViewModel = sharedViewModel
        binding.secondStepViewModel = secondStepViewModel
        binding.lifecycleOwner = this

        checkInternetConnection()

        binding.predictionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.predictionsRecyclerView.adapter = predictionsAdapter

        binding.geofenceLocationEt.doOnTextChanged { text, _, _, _ ->
            handleNextButton(text)
            getPlaces(text)
        }

        binding.step2Back.setOnClickListener {
            findNavController().navigate(R.id.action_secondStepFragment_to_firstStepFragment)
        }

        binding.step2Next.setOnClickListener {
            findNavController().navigate(R.id.action_secondStepFragment_to_thirdStepFragment)
        }

        subscribeToObservers()

        return binding.root
    }

    private fun subscribeToObservers() {
        lifecycleScope.launch {
            predictionsAdapter.placeId.collectLatest { placeId ->
                if (placeId.isNotEmpty()) {
                    onCitySelected(placeId)
                }
            }   
        }
    }

    private fun handleNextButton(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            secondStepViewModel.enableNextButton(false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkInternetConnection() {
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { online ->
                    Log.d("Internet", online.toString())
                    secondStepViewModel.setInternetAvailability(online)
                    if (online && sharedViewModel.geoCitySelected) {
                        secondStepViewModel.enableNextButton(true)
                    } else {
                        secondStepViewModel.enableNextButton(false)
                    }
                }
        }
    }

    private fun onCitySelected(placeId: String) {
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.LAT_LNG,
            Place.Field.NAME
        )
        val request = FetchPlaceRequest.builder(placeId, placeFields).build()
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                sharedViewModel.geoLatLng = response.place.latLng!!
                sharedViewModel.geoLocationName = response.place.name!!
                sharedViewModel.geoCitySelected = true
                binding.geofenceLocationEt.setText(sharedViewModel.geoLocationName)
                binding.geofenceLocationEt.setSelection(sharedViewModel.geoLocationName.length)
                binding.predictionsRecyclerView.hide()
                secondStepViewModel.enableNextButton(true)
            }
            .addOnFailureListener { exception ->
                Log.e("Step2Fragment", exception.message.toString())
            }
    }

    private fun getPlaces(text: CharSequence?) {
        if (sharedViewModel.checkDeviceLocationSettings(requireContext())) {
            lifecycleScope.launch {
                if (text.isNullOrEmpty()) {
                    predictionsAdapter.setData(emptyList())
                } else {
                    val token = AutocompleteSessionToken.newInstance()

                    val request =
                        FindAutocompletePredictionsRequest.builder()
                            .setCountries(sharedViewModel.geoCountryCode)
                            .setSessionToken(token)
                            .setQuery(text.toString())
                            .build()
                    placesClient.findAutocompletePredictions(request)
                        .addOnSuccessListener { response ->
                            predictionsAdapter.setData(response.autocompletePredictions)
                        }
                        .addOnFailureListener { exception: Exception? ->
                            if (exception is ApiException) {
                                Log.e("Step2Fragmentte", exception.message.toString())
                            }
                        }
                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Please Enable Location Settings.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}