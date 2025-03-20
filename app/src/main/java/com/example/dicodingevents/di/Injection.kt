package com.example.dicodingevents.di

import android.content.Context
import com.example.dicodingevents.data.EventsRepository
import com.example.dicodingevents.data.retrofit.ApiConfig
import com.example.dicodingevents.data.room.EventRoomDatabase
import com.example.dicodingevents.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context) : EventsRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventRoomDatabase.getDatabase(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventsRepository.getInstance(apiService, dao, appExecutors)
    }
}