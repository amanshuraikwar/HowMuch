package io.github.amanshuraikwar.howmuch.data.local.room

import android.arch.persistence.room.*

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

    @Query("SELECT * FROM Spreadsheet where year = :year AND month = :month")
    fun findByYearAndMonth(year: Int, month: Int): Spreadsheet?
}