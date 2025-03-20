package com.example.dicodingevents.ui

import androidx.lifecycle.ViewModel
import com.example.dicodingevents.data.EventsRepository
import com.example.dicodingevents.data.entity.EventsEntity

class EventsViewModel (private val eventsRepository: EventsRepository) : ViewModel() {
    fun getEvents() = eventsRepository.getEvents()

    fun getBookmarkedEvents() = eventsRepository.getBookmarkedEvent()

    fun saveEvents(events: EventsEntity){
        eventsRepository.setBookmarkedEvent(events, true)
    }

    fun deleteEvents(events: EventsEntity){
        eventsRepository.setBookmarkedEvent(events, false)
    }
}