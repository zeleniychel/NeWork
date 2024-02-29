package ru.netology.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.model.Attachment
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Coordinates
import ru.netology.nework.model.PhotoModel
import ru.netology.nework.model.Post
import ru.netology.nework.repository.post.PostRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val repository: PostRepository,
) : ViewModel() {
    private val _data = MutableLiveData<List<Post>>(emptyList())
    val data: LiveData<List<Post>> = _data

    private val _photo = MutableLiveData<PhotoModel?>(null)
    val photo: LiveData<PhotoModel?> = _photo

    private var post = Post()

    init {
        getPosts()
    }

    private fun getPosts() = viewModelScope.launch {
        _data.value = repository.getPosts()
    }

    fun likePostById(post: Post) = viewModelScope.launch {
        repository.likePostById(post)
    }

    fun setPhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

    fun save(text: String, mentionIds: List<Long>?) = viewModelScope.launch {
        repository.savePost(Post(
            content = text,
            published = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(Date())
        ))
    }

    fun removePostById(id: Long) = viewModelScope.launch {
        repository.removePostById(id)
    }

    fun setMentionIds(mentioned: List<Long>){
        post = post.copy(mentionIds = mentioned)
    }
}