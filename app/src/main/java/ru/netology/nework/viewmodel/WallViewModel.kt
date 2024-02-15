package ru.netology.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.model.Job
import ru.netology.nework.model.Post
import ru.netology.nework.repository.wall.WallRepository
import javax.inject.Inject

@HiltViewModel
class WallViewModel @Inject constructor(
    private val repository: WallRepository,
) : ViewModel() {
    private val _data = MutableLiveData<List<Post>>(emptyList())
    val data: LiveData<List<Post>> = _data

    private val _dataJobs = MutableLiveData<List<Job>>(emptyList())
    val dataJobs: LiveData<List<Job>> = _dataJobs


    fun getWall(authorId: Long) = viewModelScope.launch {
        _data.value = repository.getWall(authorId)
    }

    fun likePostById(post: Post) = viewModelScope.launch {
        repository.likePostById(post)
    }

    fun getJobs(userId: Long) = viewModelScope.launch {
        _dataJobs.value = repository.getJobs(userId)
    }
}