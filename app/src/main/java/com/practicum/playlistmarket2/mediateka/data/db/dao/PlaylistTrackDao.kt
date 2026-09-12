package com.practicum.playlistmarket2.mediateka.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.practicum.playlistmarket2.mediateka.data.db.entity.PlaylistTrackEntity
@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(playlistTrack: PlaylistTrackEntity)

    @Delete
    suspend fun deleteTrack(playlistTrack: PlaylistTrackEntity)
}