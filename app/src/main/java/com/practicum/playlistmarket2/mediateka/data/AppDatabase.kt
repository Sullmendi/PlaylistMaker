package com.practicum.playlistmarket2.mediateka.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.practicum.playlistmarket2.mediateka.data.converters.PlaylistTrackIdDbConverter
import com.practicum.playlistmarket2.mediateka.data.db.dao.PlaylistDao
import com.practicum.playlistmarket2.mediateka.data.db.dao.PlaylistTrackDao
import com.practicum.playlistmarket2.mediateka.data.db.entity.TracksEntity
import com.practicum.playlistmarket2.mediateka.data.db.dao.TracksDao
import com.practicum.playlistmarket2.mediateka.data.db.entity.PlaylistEntity
import com.practicum.playlistmarket2.mediateka.data.db.entity.PlaylistTrackEntity

@Database(version = 3, entities = [TracksEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class])
@TypeConverters(PlaylistTrackIdDbConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TracksDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistTrackDao(): PlaylistTrackDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE films_table ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `playlist_table` (\n" +
                "                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                "                `playlistName` TEXT NOT NULL, \n" +
                "                `playlistDescription` TEXT, \n" +
                "                `playlistImagePath` TEXT, \n" +
                "                `trackIdsJson` TEXT NOT NULL DEFAULT '[]', \n" +
                "                `tracksCount` INTEGER NOT NULL DEFAULT 0\n" +
                "            )")
        db.execSQL("CREATE TABLE IF NOT EXISTS `playlist_track_table` (\n" +
                "                `trackId` TEXT NOT NULL, \n" +
                "                `trackName` TEXT NOT NULL, \n" +
                "                `artistName` TEXT NOT NULL, \n" +
                "                `trackTimeMillis` INTEGER NOT NULL, \n" +
                "                `artworkUrl100` TEXT NOT NULL, \n" +
                "                `collectionName` TEXT NOT NULL, \n" +
                "                `releaseDate` TEXT, \n" +
                "                `primaryGenreName` TEXT NOT NULL, \n" +
                "                `country` TEXT NOT NULL, \n" +
                "                `previewUrl` TEXT, \n" +
                "                `isFavorite` INTEGER NOT NULL DEFAULT 0, \n" +
                "                `createdAt` INTEGER NOT NULL,\n" +
                "                PRIMARY KEY(`trackId`)" +
                "            )")
    }
}