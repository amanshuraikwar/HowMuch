package io.github.amanshuraikwar.howmuch.ui.month

import io.github.amanshuraikwar.howmuch.base.ui.base.BaseView
import io.github.amanshuraikwar.howmuch.base.ui.base.LoadingView
import io.github.amanshuraikwar.howmuch.base.ui.base.UiMessageView
import io.github.amanshuraikwar.multiitemlistadapter.ListItem

interface MonthView : BaseView, UiMessageView, LoadingView {
    fun updateMonth(previousMonth: Boolean, monthName: String, nextMonth: Boolean)
    fun submitList(list: List<ListItem<*, *>>)
    fun getInitDisplayedMonth(): Int?
    fun getInitDisplayedYear(): Int?
}