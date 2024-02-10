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
class UsersFeedViewModel@Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {

    private val _data = MutableLiveData<List<UserResponse>>(emptyList())
    val data: LiveData<List<UserResponse>> = _data

    init {
        getUsers()
    }

    private fun getUsers() = viewModelScope.launch{
        _data.value = repository.getUsers()
    }
}