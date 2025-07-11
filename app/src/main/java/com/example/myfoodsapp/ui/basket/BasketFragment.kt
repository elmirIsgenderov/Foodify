package com.example.myfoodsapp.ui.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfoodsapp.R
import com.example.myfoodsapp.utils.Resource
import com.example.myfoodsapp.databinding.FragmentBasketBinding
import com.example.myfoodsapp.room.entity.OrderItem
import com.example.myfoodsapp.ui.basket.adapter.BasketAdapter
import com.example.myfoodsapp.ui.basket.viewmodel.BasketViewModel
import com.example.myfoodsapp.retrofit.model.Basket
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class BasketFragment : Fragment() {

    private lateinit var binding: FragmentBasketBinding
    private lateinit var basketAdapter: BasketAdapter
    private val viewModel: BasketViewModel by viewModels()
    private var basketList: List<Basket> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = getCurrentUsername()
        setupRecyclerView()
        observeViewModel(username)
        setupListeners(username)
    }

    private fun getCurrentUsername(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.displayName ?: currentUser?.email ?: "anonymous"
    }

    private fun setupRecyclerView() {
        basketAdapter = BasketAdapter(
            onDeleteClick = { foodId, username ->
                viewModel.deleteFoods(foodId, username)
            },
            context = requireContext()
        )
        binding.rvBasket.apply {
            adapter = basketAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupListeners(username: String) {
        binding.btnConfirm.setOnClickListener {
            confirmOrder(username)
        }
    }

    private fun observeViewModel(username: String) {
        viewModel.basketFood.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    basketList = resource.data
                    basketAdapter.submitList(basketList)
                    viewModel.totalPrice(basketList)
                }

                is Resource.Error -> {
                    showLoading(false)
                    basketList = emptyList()
                    basketAdapter.submitList(emptyList())
                    viewModel.totalPrice(emptyList())
                }
            }
        }

        viewModel.deleteFood.observe(viewLifecycleOwner) {
            handleResource(it) { viewModel.getBasket(username) }
        }

        viewModel.clearBasket.observe(viewLifecycleOwner) {
            handleResource(it) { viewModel.getBasket(username) }
        }

        viewModel.totalPrice.observe(viewLifecycleOwner) { total ->
            updateTotalPrice(total)
        }

        viewModel.getBasket(username)
    }


    private fun handleResource(resource: Resource<*>, onSuccess: () -> Unit) {
        when (resource) {
            is Resource.Loading -> showLoading(true)
            is Resource.Success -> {
                showLoading(false)
                onSuccess()
            }

            is Resource.Error -> {
                showLoading(false)
                showToast("Xəta: ${resource.message}")
            }
        }
    }

    private fun confirmOrder(username: String) {
        val selectedItems = basketAdapter.getCurrentItems()
        if (selectedItems.isEmpty()) {
            showToast("Cart is empty")
            return
        }

        selectedItems.forEach { item ->
            val orderItem = OrderItem(
                name = item.basketFoodName,
                price = item.basketFoodPrice.toInt(),
                image = item.basketFoodPhoto,
                orderTime = Date()
            )
            viewModel.insertOrder(orderItem)
        }

        showOrderConfirmedDialog()
        basketAdapter.submitList(emptyList())
        viewModel.clearBasket(selectedItems, username)
        updateTotalPrice(0)
    }

    private fun updateTotalPrice(total: Int) {
        val deliveryPrice = binding.txtDeliverPrice.text.toString().toIntOrNull() ?: 0
        val grandTotal = deliveryPrice + total
        binding.txtTotalPrice.text = "$grandTotal"
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbCart.isVisible = isLoading
    }

    private fun showToast(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    private fun showOrderConfirmedDialog() {
        val dialogView = layoutInflater.inflate(R.layout.item_dialog, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialog.show()

        dialog.findViewById<ImageView>(R.id.imgClose)?.setOnClickListener {
            dialog.dismiss()
        }
    }
}

