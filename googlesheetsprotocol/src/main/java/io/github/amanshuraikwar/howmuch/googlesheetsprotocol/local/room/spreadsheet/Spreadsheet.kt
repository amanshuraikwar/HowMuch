package io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.room.spreadsheet

import androidx.room.Entity

@Entity(primaryKeys = ["email"])
data class Spreadsheet(var email: String,
                       var spreadsheetId: String,
                       var state: SpreadsheetState)

enum class SpreadsheetState {
    CREATED, READY
}