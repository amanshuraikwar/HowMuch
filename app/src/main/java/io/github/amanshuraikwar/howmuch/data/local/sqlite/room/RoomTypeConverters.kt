package io.github.amanshuraikwar.howmuch.data.local.sqlite.room

import androidx.room.TypeConverter
import io.github.amanshuraikwar.howmuch.data.local.sqlite.SpreadsheetState

class RoomTypeConverters {
    @TypeConverter
    fun toState(ordinal: Int) = SpreadsheetState.values()[ordinal]

    @TypeConverter
    fun toOrdinal(spreadsheetState: SpreadsheetState) = spreadsheetState.ordinal
}