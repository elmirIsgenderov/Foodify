package com.example.myfoodsapp.ui.entry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myfoodsapp.R
import com.example.myfoodsapp.databinding.FragmentEntryBinding

class EntryFragment : Fragment() {
    private lateinit var binding: FragmentEntryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_entryFragment_to_logInFragment)
        }

    }
}