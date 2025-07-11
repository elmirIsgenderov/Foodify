package com.example.myfoodsapp.ui.profile.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfoodsapp.databinding.FragmentOrdersBinding
import com.example.myfoodsapp.ui.profile.orders.adapter.OrdersAdapter
import com.example.myfoodsapp.ui.profile.orders.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment() {
    private lateinit var binding: FragmentOrdersBinding
    private val viewmodel: OrderViewModel by viewModels()
    private lateinit var ordersAdapter: OrdersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeOrders()
        viewmodel.getOrders()
    }

    private fun setupRecyclerView() {
        ordersAdapter = OrdersAdapter()
        binding.rvOrder.apply {
            adapter = ordersAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observeOrders() {
        viewmodel.orders.observe(viewLifecycleOwner) { orders ->
            ordersAdapter.submitList(orders)
        }
    }


}