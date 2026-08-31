package com.practicum.playlistmarket2.mediateka.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.practicum.playlistmarket2.mediateka.data.db.entity.TracksEntity
import com.practicum.playlistmarket2.mediateka.data.db.dao.TracksDao

@Database(version = 2, entities = [TracksEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TracksDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE films_table ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")
    }
}