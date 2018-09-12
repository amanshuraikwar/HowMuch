package io.github.amanshuraikwar.howmuch.model

data class Expense(val id: String,
                   val date: String,
                   val amount: Double,
                   val description: String,
                   val category: String)