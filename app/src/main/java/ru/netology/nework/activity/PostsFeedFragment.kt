package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentPostsFeedBinding
import javax.inject.Inject

class PostsFeedFragment: Fragment() {

    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostsFeedBinding.inflate(layoutInflater)




        return binding.root
    }

}