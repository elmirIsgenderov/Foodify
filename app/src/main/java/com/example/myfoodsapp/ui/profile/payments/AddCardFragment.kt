package com.example.myfoodsapp.ui.profile.payments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.myfoodsapp.databinding.FragmentAddCardBinding
import com.example.myfoodsapp.room.entity.CardEntity
import com.example.myfoodsapp.ui.profile.payments.viewmodel.AddCardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCardFragment : Fragment() {

    private lateinit var binding: FragmentAddCardBinding
    private val viewModel: AddCardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeViewModel()
        setupInputFormatting()
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener { saveCard() }
    }

    private fun observeViewModel() {
        viewModel.insertResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                showToast("Card saved successfully")
                clearInputFields()
            } else {
                showToast("Failed to save card")
            }
        }
    }

    private fun saveCard() {
        val cardNumber = binding.edtCardNumber.text.toString()
        val cardHolder = binding.edtCardHolder.text.toString()
        val expiryDate = binding.edtExpiryDate.text.toString()
        val cvv = binding.edtCvv.text.toString()

        if (cardNumber.isBlank() || cardHolder.isBlank() || expiryDate.isBlank() || cvv.isBlank()) {
            showToast("Enter all the information")
            return
        }

        val card = CardEntity(
            id = 0,
            cardNumber = cardNumber,
            cardHolder = cardHolder,
            expiryDate = expiryDate,
            cvv = cvv
        )
        viewModel.insertCard(card)
    }

    private fun clearInputFields() {
        with(binding) {
            edtCardNumber.text?.clear()
            edtCardHolder.text?.clear()
            edtExpiryDate.text?.clear()
            edtCvv.text?.clear()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setupInputFormatting() {
        setupCardNumberFormatting()
        setupExpiryDateFormatting()
        setupCvvBinding()
    }

    private fun setupCardNumberFormatting() {
        binding.edtCardNumber.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                val formatted = s.toString()
                    .replace(" ", "")
                    .chunked(4)
                    .joinToString(" ")

                binding.edtCardNumber.setText(formatted)
                binding.edtCardNumber.setSelection(formatted.length)
                binding.txtCartNumber.text = formatted

                isFormatting = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupExpiryDateFormatting() {
        binding.edtExpiryDate.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            private var previousText = ""

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                val input = s.toString().replace("/", "")
                if (input.length >= 2) {
                    val month = input.substring(0, 2).toIntOrNull()
                    if (month == null || month !in 1..12) {
                        binding.edtExpiryDate.setText(previousText)
                        binding.edtExpiryDate.setSelection(previousText.length)
                        isFormatting = false
                        return
                    }
                }

                val formatted = when {
                    input.length >= 3 -> input.substring(
                        0,
                        2
                    ) + "/" + input.substring(2.coerceAtMost(4))

                    input.length >= 2 -> input.substring(0, 2) + "/"
                    else -> input
                }

                previousText = formatted
                binding.edtExpiryDate.setText(formatted)
                binding.edtExpiryDate.setSelection(formatted.length)
                binding.txtExpiryDate.text = formatted

                isFormatting = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                previousText = s?.toString() ?: ""
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupCvvBinding() {
        binding.edtCvv.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.txtCvv.text = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


}
