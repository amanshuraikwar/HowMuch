package io.github.amanshuraikwar.howmuch.ui.month

import io.github.amanshuraikwar.howmuch.base.ui.base.BasePresenter

interface MonthPresenter<V : MonthView> : BasePresenter<V> {
    fun onPreviousMonthClicked()
    fun onNextMonthClicked()
    fun onRetryClicked()
    fun onRefreshClicked()
}