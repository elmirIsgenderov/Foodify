package com.example.myfoodsapp.ui.profile.profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myfoodsapp.R
import com.example.myfoodsapp.databinding.FragmentProfileBinding
import com.example.myfoodsapp.ui.profile.profile.adapter.ProfileAdapter
import com.example.myfoodsapp.ui.profile.profile.viewmodel.ProfileViewModel
import com.example.myfoodsapp.retrofit.model.ProfileOptionType
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: ProfileAdapter
    private val viewModel: ProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUserName()
        setupProfileImage()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupUserName() {
        val displayName = FirebaseAuth.getInstance().currentUser?.displayName.orEmpty()
        binding.txtProfileName.text = displayName
    }

    private fun setupProfileImage() {
        binding.imgProfile.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        loadSavedProfileImage()?.let { uri ->
            showImage(uri)
        }
    }

    private fun setupRecyclerView() {
        val navController = findNavController()

        adapter = ProfileAdapter { item ->
            when (item.type) {
                ProfileOptionType.PROFILE -> navController.navigate(R.id.yourProfileFragment)
                ProfileOptionType.PAYMENT_METHODS -> navController.navigate(R.id.paymentMehodsFragment)
                ProfileOptionType.ADDRESS -> navController.navigate(R.id.addressFragment)
                ProfileOptionType.FAVORITE -> navController.navigate(R.id.favoriteFragment)
                ProfileOptionType.ORDERS -> navController.navigate(R.id.ordersFragment)
                ProfileOptionType.LANGUAGE -> navController.navigate(R.id.languageFragment)
                ProfileOptionType.SETTINGS -> navController.navigate(R.id.settingFragment)
                ProfileOptionType.NOTIFICATIONS -> navController.navigate(R.id.notificationFragment)
                ProfileOptionType.LOGOUT -> confirmLogout()
            }
        }

        binding.rvProfil.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ProfileFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewModel.profileOption.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        viewModel.logoutSuccess.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.logInFragment)
        }
    }

    private fun confirmLogout() {
        AlertDialog.Builder(requireContext())
            .setTitle("Log out")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ -> viewModel.logout() }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                saveImageUriToPreferences(it)
                showImage(it)
            }
        }

    private fun saveImageUriToPreferences(uri: Uri) {
        val prefs = requireContext().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("profile_image_uri", uri.toString()).apply()
    }

    private fun loadSavedProfileImage(): Uri? {
        val prefs = requireContext().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        return prefs.getString("profile_image_uri", null)?.let(Uri::parse)
    }

    private fun showImage(uri: Uri) {
        Glide.with(binding.imgProfile)
            .load(uri)
            .circleCrop()
            .into(binding.imgProfile)
    }


}
