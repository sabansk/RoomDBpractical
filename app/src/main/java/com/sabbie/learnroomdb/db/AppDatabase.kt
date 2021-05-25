package com.sabbie.learnroomdb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.sabbie.learnroomdb.db.note.Note
import com.sabbie.learnroomdb.db.note.NoteDao
import androidx.room.RoomDatabase


@Database(entities = [Note::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao() : NoteDao

    companion object {
        private const val DB_NAME = "NOTE_DB"
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room
                        .databaseBuilder(
                            context,
                            AppDatabase::class.java
                        )
                        .build()


                }
            }
            return instance
        }
    }
}