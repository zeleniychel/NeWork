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
import ru.netology.nework.model.AttachModel
import ru.netology.nework.model.AttachmentType
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

    fun setAttach(attachModel: AttachModel) {
        _attach.value = attachModel
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
            val photoModel = photo.value?.file
            if (attachModel != null) {
                val url = repository.upload(attachModel)
                post.value?.let { repository.savePostWithAttachment(it, url, attach.value?.type!!) }
            } else if (photoModel != null) {
                val url = repository.saveMedia(photoModel)
                post.value?.let { repository.savePostWithAttachment(it, url, AttachmentType.IMAGE) }
                photo.value?.file?.let { repository.saveMedia(it) }
            } else {
                post.value?.let { repository.savePost(it) }
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