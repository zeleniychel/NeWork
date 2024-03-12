package ru.netology.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.PhotoModel
import ru.netology.nework.model.Post
import ru.netology.nework.repository.post.PostRepository
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

data class AttachModel(
    val inputStream: InputStream? = null,
    val type: AttachmentType? = null
)

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val repository: PostRepository,
) : ViewModel() {

    private val _photo = MutableLiveData<PhotoModel?>(null)
    val photo: LiveData<PhotoModel?> = _photo

    private val _attach = MutableLiveData<AttachModel?>(null)
    val attach: LiveData<AttachModel?> = _attach

    private var post = MutableLiveData(Post())
    val data: Flow<PagingData<Post>> = repository.data

//    init {
//        getPosts()
//    }

//    private fun getPosts() = viewModelScope.launch {
//        _data.value = repository.getPosts()
//    }

    fun likePostById(post: Post) = viewModelScope.launch {
        repository.likePostById(post)
    }

    fun setPhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

    fun setAttach(inputStream: InputStream?, type: AttachmentType?) {
        _attach.value = AttachModel(inputStream, type)
    }

    fun save(text: String) {
        val content = text.trim()
        post.value?.let {
            if (it.content == content) {
                return
            }
            post.value = it.copy(content = text)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val attachModel = attach.value
            if (attachModel == null) {
                post.value?.let { repository.savePost(it) }
            } else {
                val url = repository.upload(attachModel)
                post.value?.let { repository.savePostWithAttachment(it, url) }
            }

        }
    }
    fun edit(postEdit: Post) {
        post.value =
            postEdit.copy(published = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(Date()))
    }

    fun removePostById(id: Long) = viewModelScope.launch {
        repository.removePostById(id)
    }

    fun setMentionIds(mentioned: List<Long>) {
        post.value = post.value?.copy(mentionIds = mentioned)
    }
}