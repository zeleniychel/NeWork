package ru.netology.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.model.Post
import ru.netology.nework.repository.post.PostRepository
import javax.inject.Inject

@HiltViewModel
class PostsViewModel@Inject constructor(
    private val repository: PostRepository,
) : ViewModel() {
    private val _data = MutableLiveData<List<Post>>(emptyList())
    val data: LiveData<List<Post>> = _data

    init {
        getPosts()
    }

    private fun getPosts() = viewModelScope.launch{
        _data.value = repository.getPosts()
    }
    fun likePostById(post: Post) = viewModelScope.launch {
        repository.likePostById(post)
    }
}