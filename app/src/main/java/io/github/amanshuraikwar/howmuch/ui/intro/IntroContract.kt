package io.github.amanshuraikwar.howmuch.ui.intro

import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

/**
 * Created by amanshuraikwar on 09/03/18.
 */
interface IntroContract {

    interface View : BaseView {
        fun showError(error: String)
        fun startHomeActivity()
    }
    interface Presenter : BasePresenter<View> {
        fun onDoneClick(amount: String)
    }
}