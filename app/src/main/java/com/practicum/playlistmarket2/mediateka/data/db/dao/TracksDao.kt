package com.practicum.playlistmarket2.mediateka.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmarket2.mediateka.data.db.entity.TracksEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TracksEntity)

    @Delete
    suspend fun deleteTrack(track: TracksEntity)

    @Query("SELECT * FROM films_table ORDER BY createdAt DESC")
    suspend fun getTracks(): List<TracksEntity>

    @Query("SELECT trackId FROM films_table")
    suspend fun findFavoriteTrack(): List<String>
}