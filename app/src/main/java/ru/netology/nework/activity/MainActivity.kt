package ru.netology.nework.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.databinding.ActivityMainBinding
import ru.netology.nework.viewmodel.AuthViewModel
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)

        binding.bottomNavMenu.setOnItemSelectedListener {
            when(it.itemId){
                R.id.users -> findNavController(R.id.container).navigate(R.id.usersFeedFragment)
            }
            true
        }

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onPrepareMenu(menu: Menu) {
                menu.setGroupVisible(R.id.unauthenticated, !viewModel.authenticated)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.signin -> {
                        findNavController(R.id.container).navigate(R.id.signInFragment)
                        true
                    }

                    R.id.signup -> {
                        findNavController(R.id.container).navigate(R.id.signUpFragment)
                        true
                    }

                    else -> false
                }
            }
        })
    }
    override fun onResume() {
        super.onResume()
    }
}