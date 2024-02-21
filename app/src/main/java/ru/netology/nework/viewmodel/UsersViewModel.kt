package ru.netology.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.model.UserResponse
import ru.netology.nework.repository.User.UserRepository
import javax.inject.Inject

@HiltViewModel
class UsersViewModel@Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {

    private val _data = MutableLiveData<List<UserResponse>>(emptyList())
    val data: LiveData<List<UserResponse>> = _data

    private val _users = MutableLiveData<List<Long>>(emptyList())
    val users: LiveData<List<Long>> = _users

    init {
        getUsers()
    }

    private fun getUsers() = viewModelScope.launch{
        _data.value = repository.getUsers()
    }
    fun saveUsers(users: List<Long>) = viewModelScope.launch {
        _users.value = users
    }
}