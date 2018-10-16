package io.github.amanshuraikwar.howmuch.ui.list.expense

import io.github.amanshuraikwar.howmuch.model.Expense

interface ExpenseOnClickListener {
    fun onClick(expense: Expense)
}