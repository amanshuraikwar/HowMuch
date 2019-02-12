package io.github.amanshuraikwar.howmuch.data.local.sqlite.room.spreadsheet

import androidx.room.*

@Dao
interface SpreadsheetDao {

    @Insert
    fun insertAll(vararg spreadsheets: Spreadsheet)

    @Delete
    fun deleteAll(vararg spreadsheets: Spreadsheet)

    @Update
    fun updateAll(vararg spreadsheets: Spreadsheet)

    @Query("SELECT * FROM Spreadsheet")
    fun findAll(): List<Spreadsheet>

    @Query("SELECT * FROM Spreadsheet WHERE email = :email")
    fun findByEmail(email: String): Spreadsheet?
}