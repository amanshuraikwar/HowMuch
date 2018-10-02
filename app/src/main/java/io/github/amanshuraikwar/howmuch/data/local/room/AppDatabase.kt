package io.github.amanshuraikwar.howmuch.data.local.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

@Database(entities = [Spreadsheet::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        public const val DATABASE_NAME = "howmuch"
    }

    abstract val spreadsheetDao: SpreadsheetDao
}