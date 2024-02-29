package ru.netology.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.model.Event
import ru.netology.nework.model.EventType
import ru.netology.nework.model.PhotoModel
import ru.netology.nework.repository.event.EventRepository
import java.io.File
import javax.inject.Inject


@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository,
) : ViewModel() {
    private val _data = MutableLiveData<List<Event>>(emptyList())
    val data: LiveData<List<Event>> = _data

    private val _photo = MutableLiveData<PhotoModel?>(null)
    val photo: LiveData<PhotoModel?> = _photo

    private var event = Event()

    init {
        getEvents()
    }

    private fun getEvents() = viewModelScope.launch {
        _data.value = repository.getEvents()
    }

    fun likeEventById(event: Event) = viewModelScope.launch {
        repository.likeEventById(event)
    }

    fun setPhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

    fun setEventType(eventType: EventType){
        event = event.copy(type = eventType)
    }
    fun setEventDate(dateTime: String){
        event = event.copy(datetime = dateTime)
    }

    fun setSpeakerIds(speakers: List<Long>){
        event = event.copy(speakerIds = speakers)
    }
}