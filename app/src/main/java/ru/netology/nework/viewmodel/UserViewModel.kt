package ru.netology.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.nework.model.UserResponse
import ru.netology.nework.repository.User.UserRepository
import java.util.concurrent.Flow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
): ViewModel() {

    private val _data = MutableLiveData<List<UserResponse>>()
    val data: LiveData<List<UserResponse>> = _data

    init {
        getUsers()
    }

    private fun getUsers() = viewModelScope.launch {
        repository.getUsers().collect { users ->
            _data.value = users
        }
    }

    fun getUserById(id:Long) = viewModelScope.launch{
            repository.getUserById(id)
    }

}