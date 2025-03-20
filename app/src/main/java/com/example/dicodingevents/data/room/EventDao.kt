package com.example.dicodingevents.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dicodingevents.data.entity.EventsEntity

@Dao
interface EventDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insert(events: List<EventsEntity>)

    @Update
    fun update(events: EventsEntity)

    @Query("DELETE FROM events WHERE bookmarked = 0")
    fun deleteAll()

    @Query("SELECT * from events")
    fun getAllEvents(): LiveData<List<EventsEntity>>

    @Query("SELECT * from events WHERE bookmarked = 1")
    fun getBookmarkedEvent() : LiveData<List<EventsEntity>>

    @Query("SELECT EXISTS(SELECT * FROM events WHERE name = :name AND bookmarked = 1)")
    fun isEventBookmarked(name: String) : Boolean

}