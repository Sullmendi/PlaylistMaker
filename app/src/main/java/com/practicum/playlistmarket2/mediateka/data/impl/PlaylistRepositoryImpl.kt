package com.practicum.playlistmarket2.mediateka.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.practicum.playlistmarket2.domain.models.Playlist
import com.practicum.playlistmarket2.domain.models.Track
import com.practicum.playlistmarket2.mediateka.data.converters.PlaylistDbConverter
import com.practicum.playlistmarket2.mediateka.data.converters.PlaylistTrackDbConverter
import com.practicum.playlistmarket2.mediateka.data.converters.PlaylistTrackIdDbConverter
import com.practicum.playlistmarket2.mediateka.data.db.dao.PlaylistDao
import com.practicum.playlistmarket2.mediateka.data.db.dao.PlaylistTrackDao
import com.practicum.playlistmarket2.mediateka.data.db.entity.PlaylistCrossTrack
import com.practicum.playlistmarket2.mediateka.domain.api.TrackAddedState
import com.practicum.playlistmarket2.mediateka.domain.db.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistTrackDao: PlaylistTrackDao,
    private val converter: PlaylistDbConverter,
    private val trackConverter: PlaylistTrackDbConverter,
    private val idConverter: PlaylistTrackIdDbConverter,
    private val context: Context
): PlaylistRepository {
    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(converter.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.updatePlaylist(converter.map(playlist))
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().map{ entities ->
            entities.map{converter.map(it)}
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val trackIds = playlist.trackIds.toMutableList()

        for (trackId in trackIds){
            playlistDao.deleteTrackCross(
                PlaylistCrossTrack(id = playlist.id, trackId)
            )
        }
        playlistDao.deletePlaylist(converter.map(playlist))
        playlistDao.deleteTrackWithoutPlaylist()

    }

    override suspend fun addTrackToPlaylist(
        track: Track,
        playlist: Playlist
    ) : TrackAddedState {
        if(playlist.trackIds.contains(track.trackId)){return TrackAddedState.AlreadyAdd(playlist)
        }

        val updateIdInPlaylist = playlist.trackIds.toMutableList().apply { add(0, track.trackId) }

        val updatedPlaylist = playlist.copy(
            trackIds = updateIdInPlaylist,
            tracksCount = playlist.tracksCount + 1
        )

        playlistDao.updatePlaylist(converter.map(updatedPlaylist))

        playlistTrackDao.insertTrack(trackConverter.map(track))

        playlistDao.insertTrackCross(
            PlaylistCrossTrack(id = playlist.id, trackId = track.trackId)
        )

        return TrackAddedState.Success(updatedPlaylist)
    }

    override suspend fun deleteTrackFromPlaylist(
        track: Track,
        playlist: Playlist
    ): Playlist {
        val updateIdInPlaylist = playlist.trackIds.toMutableList().apply { remove(track.trackId) }

        val updatedPlaylist = playlist.copy(
            trackIds = updateIdInPlaylist,
            tracksCount = playlist.tracksCount - 1
        )

        playlistDao.updatePlaylist(converter.map(updatedPlaylist))

        playlistDao.deleteTrackCross(
            PlaylistCrossTrack(id = playlist.id, trackId = track.trackId)
        )

        playlistDao.deleteTrackWithoutPlaylist()


        return updatedPlaylist
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri): String = withContext(Dispatchers.IO) {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val uniqueFileName = "cover_${UUID.randomUUID()}.jpg"
        val file = File(filePath, uniqueFileName)

        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        inputStream?.close()
        outputStream.close()

        return@withContext file.absolutePath
    }

    override suspend fun getPlaylistById(playlistId: Long): Playlist {
        val playlist = playlistDao.getPlaylistById(playlistId)
        return converter.map(playlist)
    }

    override fun getTracksForPlaylist(playlistId: Long): Flow<List<Track>> {
        return playlistDao.getTracksForPlaylist(playlistId).map { playlistWithTracks ->
            if (playlistWithTracks == null) return@map emptyList<Track>()

            val jsonIds = playlistWithTracks.playlist.trackIdsJson

            val orderedIds: List<String> = idConverter.toTrackIdsList(jsonIds)

            val tracks = playlistWithTracks.tracks.map { trackEntity ->
                trackConverter.map(trackEntity)
            }

            tracks.sortedBy { track ->
                orderedIds.indexOf(track.trackId)
            }
        }
    }

}