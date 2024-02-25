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

private val empty = Event(
    0,
    0,
    "",
    null,
    null,
    "",
    "",
    "",
    null,
    EventType.ONLINE,
    listOf(),
    false,
    listOf(),
    listOf(),
    false,
    null,
    null,
    mapOf()
)

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository,
) : ViewModel() {
    private val _data = MutableLiveData<List<Event>>(emptyList())
    val data: LiveData<List<Event>> = _data

    private val _photo = MutableLiveData<PhotoModel?>(null)
    val photo: LiveData<PhotoModel?> = _photo

    private val _event = MutableLiveData(empty)
    val event: LiveData<Event> = _event

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

    fun changeEventType(eventType: EventType){
        _event.value = _event.value?.copy(type = eventType)
    }
    fun changeEventDate(dateTime: String){
        _event.value = _event.value?.copy(datetime = dateTime)
    }
}