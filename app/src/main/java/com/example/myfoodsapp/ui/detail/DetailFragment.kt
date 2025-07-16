package com.example.myfoodsapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.myfoodsapp.R
import com.example.myfoodsapp.utils.Resource
import com.example.myfoodsapp.databinding.FragmentDetailBinding
import com.example.myfoodsapp.retrofit.model.Food
import com.example.myfoodsapp.ui.detail.viewmodel.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val food = args.food

        setupUI(food)
        observeViewModel(food)
    }

    private fun setupUI(food: Food) {
        bindFoodDetails(food)
        setupQuantityButtons()
        setupAddToCartButton(food)
        setupFavoriteButton(food)
        setupBackButton()
    }

    private fun bindFoodDetails(food: Food) {
        binding.txtFoodNameDetail.text = food.foodName
        binding.txtPrice.text = food.foodPrice.toString()

        val imageUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${food.foodPhotoName}"
        Glide.with(this)
            .load(imageUrl)
            .into(binding.imgFood)
    }


    private fun setupQuantityButtons() {
        viewModel.quantity.observe(viewLifecycleOwner) { quantity ->
            binding.txtQuantity.text = quantity.toString()
        }

        binding.btnPlus.setOnClickListener {
            viewModel.increaseQuantity()
        }

        binding.btnMinus.setOnClickListener {
            viewModel.decreaseQuantity()
        }
    }

    private fun setupAddToCartButton(food: Food) {
        binding.btnAddCard.setOnClickListener {
            val unitPrice = food.foodPrice
            val totalPrice = viewModel.calculateTotalPrice(unitPrice)
            showAddToCartDialog(food, totalPrice)
        }
    }

    private fun showAddToCartDialog(food: Food, totalPrice: Int) {
        val quantity = viewModel.quantity.value ?: 1
        val currentUser = FirebaseAuth.getInstance().currentUser
        val username = currentUser?.displayName ?: currentUser?.email ?: "anonymous"

        AlertDialog.Builder(requireContext())
            .setTitle("Basket")
            .setMessage("Total price: $totalPrice ₺. Do you want to add this product to cart?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.addToCart(
                    foodName = food.foodName,
                    foodPhotoName = food.foodPhotoName,
                    foodPrice = food.foodPrice,
                    foodOrderQuantity = quantity,
                    username = username
                )
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun setupFavoriteButton(food: Food) {
        viewModel.isFavorite.observe(viewLifecycleOwner) { isFav ->
            val iconRes = if (isFav) R.drawable.ic_like else R.drawable.ic_like_empty
            binding.imgLike.setImageResource(iconRes)
        }

        viewModel.checkIfFavorite(food.foodId)

        binding.imgLike.setOnClickListener {
            viewModel.toggleFavorite(food)
        }
    }

    private fun setupBackButton() {
        binding.imgBackDetail.setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
        }
    }

    private fun observeViewModel(food: Food) {
        viewModel.addToCart.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    Snackbar.make(
                        binding.root,
                        "${food.foodName} added to cart",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                is Resource.Error -> {
                    hideLoading()
                    Snackbar.make(
                        binding.root,
                        "Error: ${result.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showLoading() {
        binding.pbDetail.isVisible = true
    }

    private fun hideLoading() {
        binding.pbDetail.isVisible = false
    }
}
