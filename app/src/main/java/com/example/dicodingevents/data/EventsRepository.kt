package com.example.dicodingevents.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.bumptech.glide.util.Executors
import com.example.dicodingevents.data.entity.EventsEntity
import com.example.dicodingevents.data.response.DicodingEventsResponse
import com.example.dicodingevents.data.response.ListEventsItem
import com.example.dicodingevents.data.retrofit.ApiService
import com.example.dicodingevents.data.room.EventDao
import com.example.dicodingevents.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.PrivateKey

class EventsRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
){
    private val result = MediatorLiveData<Result<List<EventsEntity>>>()

    fun getEvents(): LiveData<Result<List<EventsEntity>>>{
        result.value = Result.Loading
        val client = apiService.getEvent(1)
        client.enqueue(object : Callback<DicodingEventsResponse>{
            override fun onResponse(
                call: Call<DicodingEventsResponse>,
                response: Response<DicodingEventsResponse>
            ) {
                if (response.isSuccessful){
                    val events = response.body()?.listEvents
                    val eventsList = ArrayList<EventsEntity>()
                    appExecutors.diskIO.execute{
                        events?.forEach{ event ->
                            val isBookmarked = eventDao.isEventBookmarked(event.name)
                            val events = EventsEntity(
                                event.id,
                                event.name,
                                event.mediaCover,
                                isBookmarked
                            )
                            eventsList.add(events)
                        }
                        eventDao.deleteAll()
                        eventDao.insert(eventsList)
                    }
                }
            }

            override fun onFailure(call: Call<DicodingEventsResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val localData = eventDao.getAllEvents()
        result.addSource(localData) {newData: List<EventsEntity> ->
            result.value = Result.Success(newData)
        }
        return result
    }

    fun getBookmarkedEvent() : LiveData<List<EventsEntity>>{
        return eventDao.getBookmarkedEvent()
    }

    fun setBookmarkedEvent(events: EventsEntity, bookmarkedState: Boolean){
        appExecutors.diskIO.execute{
            events.bookmarked = bookmarkedState
            eventDao.update(events)
        }
    }

    companion object {
        @Volatile
        private var instance: EventsRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ) : EventsRepository =
            instance ?: synchronized(this){
                instance ?: EventsRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}