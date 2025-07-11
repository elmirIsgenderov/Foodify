package com.example.myfoodsapp.ui.profile.yourprofile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myfoodsapp.R
import com.example.myfoodsapp.databinding.FragmentYourProfileBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class YourProfileFragment : Fragment() {

    private lateinit var binding: FragmentYourProfileBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentYourProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setupUserInfo()
        setupImageSection()
        setupUpdateButton()
    }

    private fun setupUserInfo() {
        val user = auth.currentUser
        user?.let {
            binding.edtName.setText(it.displayName.orEmpty())
            binding.edtGmail.setText(it.email)

            binding.edtName.isEnabled = false
            binding.edtGmail.isEnabled = false
        }
    }

    private fun setupImageSection() {
        binding.imgProfile.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        loadImageFromPreferences()?.let { uri ->
            showImage(uri)
        }
    }
    private fun setupUpdateButton() {
        binding.btnUpdate.setOnClickListener {
            val currentPassword = binding.edtCurrentPassword.text.toString().trim()
            val newPassword = binding.edtNewPassword.text.toString().trim()
            val user = auth.currentUser

            if (!validateInputs(currentPassword, newPassword, user)) return@setOnClickListener

            val credential = EmailAuthProvider.getCredential(user!!.email!!, currentPassword)
            user.reauthenticate(credential)
                .addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        updatePassword(user, newPassword)
                    } else {
                        showToast("The current password is incorrect") // 🔁 strings.xml faylına əlavə etmək olar
                    }
                }
        }
    }

    private fun validateInputs(
        currentPassword: String,
        newPassword: String,
        user: FirebaseUser?
    ): Boolean {
        return when {
            currentPassword.isBlank() || newPassword.isBlank() -> {
                showToast("Fill in all fields")
                false
            }

            newPassword.length < 6 -> {
                showToast("Password must be at least 6 characters long")
                false
            }

            user?.email.isNullOrEmpty() -> {
                showToast("User information not found")
                false
            }

            else -> true
        }
    }

    private fun updatePassword(user: FirebaseUser, newPassword: String) {
        user.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Password changed successfully")
                    binding.edtCurrentPassword.text?.clear()
                    binding.edtNewPassword.text?.clear()
                } else {
                    showToast("Password could not be changed")
                }
            }
    }


    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                saveImageUriToPreferences(it)
                showImage(it)
            }
        }

    private fun showImage(uri: Uri) {
        Glide.with(binding.imgProfile)
            .load(uri)
            .circleCrop()
            .into(binding.imgProfile)
    }

    private fun saveImageUriToPreferences(uri: Uri) {
        val prefs = requireContext().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("profile_image_uri", uri.toString()).apply()
    }


    private fun loadImageFromPreferences(): Uri? {
        val prefs = requireContext().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        return prefs.getString("profile_image_uri", null)?.let(Uri::parse)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}