package com.example.myfoodsapp.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myfoodsapp.R
import com.example.myfoodsapp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        btnSignUp()
        navigateToLogin()
    }

    private fun btnSignUp() {

        binding.btnSignup.setOnClickListener {
            val username = binding.edtNameSignup.text.toString()
            val email = binding.edtEmailSignup.text.toString()
            val password = binding.edtPasswordSignup.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        val user = authResult.user
                        if (user != null) {
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build()

                            user.updateProfile(profileUpdates)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        firebaseAuth.signOut()
                                        Toast.makeText(
                                            requireContext(),
                                            "Registration successful",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)
                                    }
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToLogin() {
        binding.txtLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)
        }
    }
}