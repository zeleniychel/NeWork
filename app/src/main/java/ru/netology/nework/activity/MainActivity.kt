package ru.netology.nework.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R

import ru.netology.nework.databinding.ActivityMainBinding
import ru.netology.nework.viewmodel.AuthViewModel
@AndroidEntryPoint

class MainActivity : AppCompatActivity() {
    val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavMenu.setupWithNavController(navController)


        fun replaceFragment(fragment: Fragment){
            val transaction = supportFragmentManager.beginTransaction().replace(R.id.container,fragment)
            transaction.commit()
        }
        if (savedInstanceState == null) {
            replaceFragment(PostsFeedFragment())
        }

        binding.bottomNavMenu.setOnItemSelectedListener {
            when(it.itemId){
                R.id.posts -> replaceFragment(PostsFeedFragment())
                R.id.events -> replaceFragment(EventsFeedFragment())
                R.id.users -> replaceFragment(UsersFeedFragment())
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
                        navController.navigate(R.id.signInFragment)
                        true
                    }

                    R.id.signup -> {
                        navController.navigate(R.id.signUpFragment)
                        true
                    }

                    else -> false
                }
            }
        })
    }
}