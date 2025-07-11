package com.example.myfoodsapp.ui.profile.payments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfoodsapp.R
import com.example.myfoodsapp.databinding.FragmentPaymentMehodsBinding
import com.example.myfoodsapp.ui.profile.payments.adapter.PaymentsAdapter
import com.example.myfoodsapp.ui.profile.payments.viewmodel.PaymentsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentMethodsFragment : Fragment() {
    private lateinit var binding: FragmentPaymentMehodsBinding
    private val viewModel: PaymentsViewModel by viewModels()
    private lateinit var paymentsAdapter: PaymentsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentMehodsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observePayments()
        setupListeners()

        viewModel.getPayments()
    }

    private fun setupRecyclerView() {
        paymentsAdapter = PaymentsAdapter()
        binding.rvPayment.apply {
            adapter = paymentsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observePayments() {
        viewModel.payments.observe(viewLifecycleOwner) { paymentList ->
            paymentsAdapter.submitList(paymentList)
        }
    }

    private fun setupListeners() {
        binding.btnAddNewCard.setOnClickListener {
            findNavController().navigate(R.id.action_paymentMehodsFragment_to_addCardFragment)
        }
    }

}