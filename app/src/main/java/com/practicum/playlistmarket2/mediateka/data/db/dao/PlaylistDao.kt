package com.practicum.playlistmarket2.mediateka.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.practicum.playlistmarket2.mediateka.data.db.entity.PlaylistCrossTrack
import com.practicum.playlistmarket2.mediateka.data.db.entity.PlaylistEntity
import com.practicum.playlistmarket2.mediateka.data.db.entity.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackCross(crossRef: PlaylistCrossTrack)

    @Delete
    suspend fun deleteTrackCross(crossRef: PlaylistCrossTrack)

    @Transaction
    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    fun getTracksForPlaylist(playlistId: Long): Flow<PlaylistWithTracks?>

    @Query("DELETE FROM playlist_track_table WHERE trackId NOT IN (SELECT DISTINCT trackId FROM playlist_cross_track)")
    suspend fun deleteTrackWithoutPlaylist()
}