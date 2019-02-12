package io.github.amanshuraikwar.howmuch.data.network.sheets

import java.lang.Exception

open class SpreadSheetException(message: String) : Exception(message)

class InvalidTransactionEntryException(message: String, cellRange: String, sheet: String)
    : SpreadSheetException(message)

class NoCategoriesFoundException(message: String) : SpreadSheetException(message)

class InvalidCategoryException(message: String) : SpreadSheetException(message)