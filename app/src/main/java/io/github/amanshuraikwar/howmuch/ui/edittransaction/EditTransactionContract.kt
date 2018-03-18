package io.github.amanshuraikwar.howmuch.ui.edittransaction

import io.github.amanshuraikwar.howmuch.data.local.room.transaction.Transaction
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

/**
 * Created by amanshuraikwar on 10/03/18.
 */
interface EditTransactionContract {

    interface View : BaseView {
        fun showError(error: String)
        fun closeActivity()
        fun getCurTransaction(): Transaction
    }

    interface Presenter : BasePresenter<View> {
        fun onDoneBtnClick(amount: String, transactionType: String)
    }
}