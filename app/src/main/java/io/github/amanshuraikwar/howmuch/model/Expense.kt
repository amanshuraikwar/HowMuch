package io.github.amanshuraikwar.howmuch.model

data class Expense(val id: String,
                   val date: String,
                   val time: String,
                   val amount: String,
                   val description: String,
                   val category: String)