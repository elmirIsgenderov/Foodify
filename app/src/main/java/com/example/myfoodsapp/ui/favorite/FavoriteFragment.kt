package com.example.myfoodsapp.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myfoodsapp.databinding.FragmentFavoriteBinding
import com.example.myfoodsapp.ui.favorite.adapter.FavoriteAdapter
import com.example.myfoodsapp.ui.favorite.viewmodel.FavoriteViewModel
import com.example.myfoodsapp.retrofit.model.Food
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val viewModel: FavoriteViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupAdapter() {
        favoriteAdapter = FavoriteAdapter(
            itemClick = ::navigateToFoodDetail,
            onDeleteClick = ::confirmDeleteFavorite
        )
    }

    private fun setupRecyclerView() {
        binding.rvFavorite.apply {
            adapter = favoriteAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewModel.favoriteFoods.observe(viewLifecycleOwner) { foodList ->
            favoriteAdapter.submitList(foodList)
        }
        viewModel.fetchFavoriteFoods()
    }

    private fun confirmDeleteFavorite(food: Food) {
        AlertDialog.Builder(requireContext())
            .setTitle("Remove from Favorites")
            .setMessage("Do you want to remove ${food.foodName} from your favorites?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.removeFromFavorites(food.foodId)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun navigateToFoodDetail(food: Food) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(food)
        findNavController().navigate(action)
    }
}