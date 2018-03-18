package io.github.amanshuraikwar.howmuch.ui.edittransaction

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.local.room.transaction.Transaction
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.util.LogUtil
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by amanshuraikwar on 09/03/18.
 */
class EditTransactionPresenter @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<EditTransactionContract.View>(appBus, dataManager),
        EditTransactionContract.Presenter {

    private val TAG = LogUtil.getLogTag(this)

    override fun onDoneBtnClick(amount: String, transactionType: String) {

        with(amount.trim()) {
            when {
                !Util.isAmountValid(amount) -> getView()?.showError("Please enter a valid amount!")
                transactionType == "-" -> editTransaction(this.toInt())
                else -> editTransaction(this.toInt())
            }
        }
    }

    private fun editTransaction(amount: Int) {
        getView()?.getCurTransaction()?.let {

            val transaction = Transaction(
                    it.getId(),
                    it.getDateAdded(),
                    amount,
                    "")

            getDataManager()
                    .updateTransaction(transaction)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                if (it) {
                                    getAppBus().onTransactionsChanged.onNext(transaction)
                                    getView()?.closeActivity()
                                }
                            },
                            {
                                getView()?.showError("Could not save!")
                            }
                    ).addToCleanup()
        }
    }

}