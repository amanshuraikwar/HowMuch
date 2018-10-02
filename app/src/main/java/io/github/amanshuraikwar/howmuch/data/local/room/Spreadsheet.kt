package io.github.amanshuraikwar.howmuch.data.local.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(primaryKeys = ["year","month"])
data class Spreadsheet(var year: Int,
                       var month: Int,
                       var spreadsheetId: String)