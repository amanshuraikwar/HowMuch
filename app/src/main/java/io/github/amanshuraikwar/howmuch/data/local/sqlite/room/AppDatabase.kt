package io.github.amanshuraikwar.howmuch.data.local.sqlite.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import io.github.amanshuraikwar.howmuch.data.local.sqlite.room.spreadsheet.Spreadsheet
import io.github.amanshuraikwar.howmuch.data.local.sqlite.room.spreadsheet.SpreadsheetDao
import io.github.amanshuraikwar.howmuch.data.local.sqlite.room.spreadsheet.SpreadsheetState

@Database(entities = [Spreadsheet::class], version = 4)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "howmuch"
    }

    abstract val spreadsheetDao: SpreadsheetDao
}

class RoomTypeConverters {
    @TypeConverter
    fun toState(ordinal: Int) = SpreadsheetState.values()[ordinal]

    @TypeConverter
    fun toOrdinal(spreadsheetState: SpreadsheetState) = spreadsheetState.ordinal
}