package io.github.amanshuraikwar.howmuch.data.local.sqlite.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import io.github.amanshuraikwar.howmuch.data.local.sqlite.Spreadsheet

@Database(entities = [Spreadsheet::class], version = 2)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        public const val DATABASE_NAME = "howmuch"
    }

    abstract val spreadsheetDao: SpreadsheetDao
}