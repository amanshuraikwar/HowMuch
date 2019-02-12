package io.github.amanshuraikwar.howmuch.ui.base

interface UiMessageView {
    fun showToast(message: String)
    fun showSnackbar(message: String)
}