package io.github.amanshuraikwar.howmuch.ui.addtransaction

import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

/**
 * Created by amanshuraikwar on 10/03/18.
 */
interface AddTransactionContract {

    interface View : BaseView {
        fun showError(error: String)
        fun closeActivity()
    }

    interface Presenter : BasePresenter<View> {
        fun onAddBtnClick(amount: String)
    }
}