package ru.netology.nework.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.ActivityMainBinding
import ru.netology.nework.viewmodel.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var appAuth: AppAuth
    val viewModel by viewModels<AuthViewModel>()
    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
navController = findNavController(R.id.container)

        binding.bottomNavMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.users -> findNavController(R.id.container).navigate(R.id.usersFeedFragment)
                R.id.posts -> findNavController(R.id.container).navigate(R.id.postsFeedFragment)
                R.id.events -> findNavController(R.id.container).navigate(R.id.eventFeedFragment)
            }
            true
        }
        findNavController(R.id.container).addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signUpFragment,
                R.id.signInFragment,
                R.id.newEventFragment,
                R.id.newPostFragment,
                R.id.postFragment,
                R.id.eventFragment-> {
                    binding.bottomNavMenu.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavMenu.visibility = View.VISIBLE
                }
            }
        }

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)


            }

            override fun onPrepareMenu(menu: Menu) {
                menu.setGroupVisible(R.id.authenticated, viewModel.authenticated)
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

                    R.id.signout -> {
                        appAuth.removeAuth()
                        true
                    }

                    else -> false
                }
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        navController.removeOnDestinationChangedListener { _, _, _ -> }
    }
}