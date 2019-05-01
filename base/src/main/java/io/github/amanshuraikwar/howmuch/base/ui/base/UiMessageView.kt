package io.github.amanshuraikwar.howmuch.base.ui.base

interface UiMessageView {
    fun showToast(message: String)
    fun showSnackbar(message: String)
    fun showError(message: String) = showToast(message)
}