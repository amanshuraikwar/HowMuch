package io.github.amanshuraikwar.howmuch.ui.settings

import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

interface SettingsContract {

    interface View : BaseView {
        fun initiateSignOut()
        fun updateCurrency(currency: String)
        fun updateProfilePic(url: String)
        fun updateName(name: String)
        fun updateEmail(email: String)
        fun showCurrencyDialog(currencyList: List<String>)
    }

    interface Presenter : BasePresenter<View> {
        fun onCurrencyClicked()
        fun onAboutClicked()
        fun onLogoutClicked()
        fun onCurrencySelected(which: String)
    }
}