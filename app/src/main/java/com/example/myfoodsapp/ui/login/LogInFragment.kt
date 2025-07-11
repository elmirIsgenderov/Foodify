package com.example.myfoodsapp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myfoodsapp.R
import com.example.myfoodsapp.databinding.FragmentLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        btnLogin()
        checkLogin()
        navigateToSignUp()
    }

    private fun btnLogin() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString()
            val password = binding.edtPasswordLogin.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.entryFragment, true)
                            .build()
                        findNavController().navigate(R.id.homeFragment, null, navOptions)
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Email or password is incorrect",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Email and password cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun checkLogin() {
        if (firebaseAuth.currentUser != null) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.entryFragment, true)
                .build()
            findNavController().navigate(R.id.homeFragment, null, navOptions)
        }
    }

    private fun navigateToSignUp() {
        binding.txtSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }
    }
}