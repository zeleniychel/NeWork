package ru.netology.nework.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.error.ApiError
import ru.netology.nework.repository.User.UserRepository
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: UserRepository,
    private val appAuth: AppAuth,
) : ViewModel() {

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _authentication: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val authentication: StateFlow<Boolean> = _authentication


    fun updateUser(login: String, password: String) = viewModelScope.launch {
        try {
            val user = repository.updateUser(login, password)
            user.token?.let { appAuth.setAuth(user.id, it) }
            _authentication.value = appAuth.authStateFlow.value.id != 0L
        } catch (e: Exception) {


            when (e) {
                is IOException -> {
                    _errorMessage.value = "Network error"
                }

                is ApiError -> {
                    when (e.code) {
                        "404" -> {
                            _errorMessage.value = "User not found"
                        }

                        else -> {
                            _errorMessage.value = "Api error"
                        }
                    }
                }

                else -> {
                    _errorMessage.value = "Unknown error"
                }
            }
        }
    }
}