package io.github.amanshuraikwar.howmuch.data.local.sqlite

import androidx.room.Entity

@Entity(primaryKeys = ["year", "month", "email"])
data class Spreadsheet(var year: Int,
                       var month: Int,
                       var email: String,
                       var spreadsheetId: String,
                       var state: SpreadsheetState)

enum class SpreadsheetState {
    CREATED, READY
}