package com.practicum.playlistmarket2.mediateka.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmarket2.mediateka.data.db.entity.PlaylistTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(playlistTrack: PlaylistTrackEntity)

    @Delete
    suspend fun deleteTrack(playlistTrack: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track_table WHERE trackId IN (:ids)")
    fun getTracks(ids: List<String>): Flow<List<PlaylistTrackEntity>>

    @Query("DELETE FROM playlist_track_table WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: String)
}