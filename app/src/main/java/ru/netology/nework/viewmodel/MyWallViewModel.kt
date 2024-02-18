package ru.netology.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.model.Job
import ru.netology.nework.model.Post
import ru.netology.nework.repository.mywall.MyWallRepository
import javax.inject.Inject

@HiltViewModel
class MyWallViewModel @Inject constructor(
    private val repository: MyWallRepository,
) : ViewModel() {
    private val _data = MutableLiveData<List<Post>>(emptyList())
    val data: LiveData<List<Post>> = _data

    private val _dataJobs = MutableLiveData<List<Job>>(emptyList())
    val dataJobs: LiveData<List<Job>> = _dataJobs


    fun getMyWall() = viewModelScope.launch {
        _data.value = repository.getMyWall()
    }

    fun likePostById(post: Post) = viewModelScope.launch {
        repository.likeMyPostById(post)
    }

    fun getMyJobs() = viewModelScope.launch {
        _dataJobs.value = repository.getMyJobs()
    }

    fun saveMyJob(job: Job) = viewModelScope.launch {
        repository.saveMyJob(job)
    }
    fun deleteMyJob(id:Long) = viewModelScope.launch {
        repository.deleteMyJob(id)
    }
}