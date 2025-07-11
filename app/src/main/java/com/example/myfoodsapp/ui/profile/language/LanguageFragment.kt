package com.example.myfoodsapp.ui.profile.language

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfoodsapp.databinding.FragmentLanguageBinding
import com.example.myfoodsapp.ui.profile.language.adapter.LanguageAdapter
import com.example.myfoodsapp.ui.profile.language.viewmodel.LanguageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : Fragment() {
    private lateinit var binding: FragmentLanguageBinding
    private lateinit var languageAdapter: LanguageAdapter
    private val viewModel: LanguageViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLanguageAdapter()
        observeLanguageList()
        observeCurrentLanguage()
    }

    private fun setupLanguageAdapter() {
        languageAdapter = LanguageAdapter { selectedLanguage ->
            if (selectedLanguage != viewModel.currentLanguage.value) {
                viewModel.onLanguageSelected(selectedLanguage)
                requireActivity().recreate()
            }
        }

        binding.rvLanguage.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = languageAdapter
        }
    }

    private fun observeLanguageList() {
        viewModel.languageList.observe(viewLifecycleOwner) { languages ->
            languageAdapter.submitList(languages)
        }
    }

    private fun observeCurrentLanguage() {
        viewModel.currentLanguage.observe(viewLifecycleOwner) { currentLangCode ->
            languageAdapter.setSelectedLanguage(currentLangCode)
        }
    }

}