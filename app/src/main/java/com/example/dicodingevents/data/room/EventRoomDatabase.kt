package com.example.dicodingevents.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dicodingevents.data.entity.EventsEntity

@Database(entities = [EventsEntity::class], version = 1)
abstract class EventRoomDatabase : RoomDatabase() {
    abstract fun eventDao() : EventDao

    companion object{
        @Volatile
        private var INSTANCE: EventRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context) : EventRoomDatabase {
            if (INSTANCE == null){
                synchronized(EventRoomDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, EventRoomDatabase::class.java, "event_database").build()
                }
            }
            return INSTANCE as EventRoomDatabase
        }
    }
}