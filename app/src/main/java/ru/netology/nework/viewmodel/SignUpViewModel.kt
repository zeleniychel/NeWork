package ru.netology.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.error.ApiError
import ru.netology.nework.model.PhotoModel
import ru.netology.nework.repository.User.UserRepository
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: UserRepository,
    private val appAuth: AppAuth,
) : ViewModel() {

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _authentication: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val authentication: StateFlow<Boolean> = _authentication

    private val _photo = MutableLiveData<PhotoModel?>(null)
    val photo: LiveData<PhotoModel?> = _photo


    fun setPhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

    fun registerUser(login: String, password: String, name: String) =
        viewModelScope.launch {
            try {
                val user = repository.registerUser(login, password, name)
                user.token?.let { appAuth.setAuth(user.id, it) }
                _authentication.value = appAuth.authStateFlow.value.id != 0L
            } catch (e: Exception) {
                when (e) {
                    is IOException -> {
                        _errorMessage.value = "Network error"
                    }

                    is ApiError -> {
                        _errorMessage.value = "Api error"
                    }

                    else -> {
                        _errorMessage.value = "Unknown error"
                    }
                }
            }
        }

    fun registerUserWithAvatar(login: String, password: String, name: String, photoModel: PhotoModel) =
        viewModelScope.launch {
            try {
                val user = repository.registerUserWithAvatar(login, password, name, photoModel.file!!)
                user.token?.let { appAuth.setAuth(user.id, it) }
                _authentication.value = appAuth.authStateFlow.value.id != 0L
            } catch (e: Exception) {
                when (e) {
                    is IOException -> {
                        _errorMessage.value = "Network error"
                    }

                    is ApiError -> {
                        _errorMessage.value = "Api error"
                    }

                    else -> {
                        _errorMessage.value = "Unknown error"
                    }
                }
            }
        }
}