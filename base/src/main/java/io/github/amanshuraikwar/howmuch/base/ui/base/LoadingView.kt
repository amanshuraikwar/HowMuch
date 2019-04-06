package io.github.amanshuraikwar.howmuch.base.ui.base

interface LoadingView {
    fun showLoading(message: String)
    fun hideLoading()
    fun showError(message: String)
}