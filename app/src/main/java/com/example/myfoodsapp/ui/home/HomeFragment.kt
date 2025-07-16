package com.example.myfoodsapp.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfoodsapp.utils.LocationHelper
import com.example.myfoodsapp.utils.Resource
import com.example.myfoodsapp.databinding.FragmentHomeBinding
import com.example.myfoodsapp.ui.home.adapter.PopularAdapter
import com.example.myfoodsapp.ui.home.adapter.RecentAdapter
import com.example.myfoodsapp.ui.home.viewmodel.HomeViewModel
import com.example.myfoodsapp.retrofit.model.Food
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var recentAdapter: RecentAdapter
    private lateinit var popularAdapter: PopularAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var locationHelper: LocationHelper
    private var searchJob: Job? = null

    companion object {
        private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLocation()
        setupUI()
        observeViewModel()
    }

    private fun initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationHelper = LocationHelper(requireContext(), fusedLocationClient)
        registerPermissionLauncher()
        checkLocationPermission()
    }

    private fun registerPermissionLauncher() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                requestLocation()
            } else {
                showError("İcazə verilmədi")
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), LOCATION_PERMISSION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            requestLocation()
        } else {
            permissionLauncher.launch(LOCATION_PERMISSION)
        }
    }

    private fun requestLocation() {
        locationHelper.getUserLocation(
            onResult = { cityText ->
                binding.txtLocation.text = cityText
            },
            onError = { errorMessage ->
                showError(errorMessage)
            }
        )
    }

    private fun setupUI() {
        setupRecyclerViews()
        setupSearchListener()
        fetchData()
    }

    private fun setupRecyclerViews() {
        recentAdapter = RecentAdapter { food -> navigateToDetail(food) }
        binding.rvRecent.apply {
            adapter = recentAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        popularAdapter = PopularAdapter(emptyList())
        binding.rvPopular.apply {
            adapter = popularAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSearchListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.getSearchFood(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(300)
                    newText?.let { viewModel.getSearchFood(it) }
                }
                return true
            }
        })
    }

    private fun fetchData() {
        viewModel.getCurrentFoods()
        viewModel.getPopularRestaurants()
    }

    private fun observeViewModel() {
        viewModel.currentFoods.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    recentAdapter.submitList(result.data)
                }

                is Resource.Error -> {
                    hideLoading()
                    showError(result.message)
                }
            }
        }

        viewModel.searchResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    recentAdapter.submitList(result.data)
                }

                is Resource.Error -> {
                    hideLoading()
                    showError(result.message)
                }
            }
        }

        viewModel.popularRestaurants.observe(viewLifecycleOwner) {
            popularAdapter.updateData(it)
        }

    }

    private fun navigateToDetail(food: Food) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(food)
        findNavController().navigate(action)
    }

    private fun showError(message: String?) {
        Snackbar.make(binding.root, message ?: "Xəta baş verdi", Snackbar.LENGTH_LONG).show()
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
    }

    private fun hideLoading() {
        binding.progressBar.isVisible = false
    }
}
