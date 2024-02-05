package ru.netology.nework.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.netology.nework.auth.AppAuth
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    appAuth: AppAuth,
) : ViewModel() {
    val data= appAuth.authStateFlow

    val authenticated: Boolean
        get() = data.value.id != 0L
}