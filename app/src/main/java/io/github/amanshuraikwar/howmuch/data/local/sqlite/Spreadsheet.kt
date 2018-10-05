package io.github.amanshuraikwar.howmuch.data.local.sqlite

import android.arch.persistence.room.Entity

@Entity(primaryKeys = ["year","month"])
data class Spreadsheet(var year: Int,
                       var month: Int,
                       var spreadsheetId: String,
                       var state: SpreadsheetState)

enum class SpreadsheetState {
    CREATED, READY
}