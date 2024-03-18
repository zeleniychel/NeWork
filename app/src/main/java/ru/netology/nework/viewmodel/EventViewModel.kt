package ru.netology.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.netology.nework.model.Event
import ru.netology.nework.model.EventType
import ru.netology.nework.model.PhotoModel
import ru.netology.nework.repository.event.EventRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository,
) : ViewModel() {
    val data: Flow<PagingData<Event>> = repository.data

    private val _photo = MutableLiveData<PhotoModel?>(null)
    val photo: LiveData<PhotoModel?> = _photo

    private var event = MutableLiveData(Event())

//    init {
//        getEvents()
//    }
//
//    private fun getEvents() = viewModelScope.launch {
//        _data.value = repository.getEvents()
//    }
    fun save(text: String) {
        val content = text.trim()
        event.value?.let {
            if (it.content == content) {
                return
            }
            event.value = it.copy(content = text)
        }
        viewModelScope.launch {
            if (_photo.value == null) {
                event.value?.let { repository.saveEvent(it) }
            } else {
                event.value?.let { repository.saveEventWithAttachment(it, _photo.value!!) }
            }
        }
    }

    fun removeEventById(id: Long) = viewModelScope.launch {
        repository.removeEventById(id)
    }

    fun likeEventById(event: Event) = viewModelScope.launch {
        repository.likeEventById(event)
    }

    fun setPhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

    fun edit(eventEdit:Event) {
        event.value = eventEdit.copy(published = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(
            Date()
        ))
    }

    fun setEventType(eventType: EventType){
        event.value = event.value?.copy(type = eventType)
    }
    fun setEventDate(dateTime: String){
        event.value = event.value?.copy(datetime = dateTime)
    }

    fun setSpeakerIds(speakers: List<Long>){
        event.value = event.value?.copy(speakerIds = speakers)
    }
}