package io.github.amanshuraikwar.howmuch.data.local.sqlite.room

import android.arch.persistence.room.*
import io.github.amanshuraikwar.howmuch.data.local.sqlite.Spreadsheet

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

    @Query("SELECT * FROM Spreadsheet where year = :year AND month = :month AND email = :email")
    fun findByYearAndMonthAndEmail(year: Int, month: Int, email: String): Spreadsheet?
}