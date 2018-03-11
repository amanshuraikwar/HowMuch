package io.github.amanshuraikwar.howmuch.data.model

import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime

/**
 * Created by amanshuraikwar on 09/03/18.
 */
data class DayExpense(val date: LocalDate, val amount: Int, val transactionNum: Int)