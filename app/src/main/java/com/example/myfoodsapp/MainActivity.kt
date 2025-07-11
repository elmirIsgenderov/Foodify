package com.example.myfoodsapp

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.myfoodsapp.utils.LocaleUtil.setLocale
import com.example.myfoodsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
        setupNavigation()
    }

    override fun attachBaseContext(newBase: Context) {
        val sharedPref = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val langCode = sharedPref.getString("lang", "az") ?: "az"
        val context = setLocale(newBase, langCode)
        super.attachBaseContext(context)
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNav, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in visibleFragments) {
                showBottomNav()
            } else {
                hideBottomNav()
            }
        }
    }

    private val visibleFragments = setOf(
        R.id.homeFragment,
        R.id.favoriteFragment,
        R.id.basketFragment,
        R.id.profileFragment
    )

    private fun showBottomNav() {
        binding.bottomNav.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNav.visibility = View.GONE
    }
}
