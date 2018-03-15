package io.github.amanshuraikwar.howmuch.ui.addtransaction

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
class AddTransactionPresenter @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<AddTransactionContract.View>(appBus, dataManager),
        AddTransactionContract.Presenter {

    private val TAG = LogUtil.getLogTag(this)

    private var disposables: Set<Disposable> = mutableSetOf()

    override fun onDetach() {
        super.onDetach()

        for (disposable in disposables) {
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
    }

    override fun onAddBtnClick(amount: String) {

        with(amount.trim()) {
            if (this == "" || this == "0") {
                getView()?.showError("Please enter a valid amount!")
            } else {
                addTransaction(this.toInt())
            }
        }
    }

    private fun addTransaction(amount: Int) {

        disposables.plusElement(
                getDataManager()
                        .addTransaction(
                                Transaction(
                                        Util.getCurDateTime(),
                                        amount,
                                        ""))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    if (it) {
                                        getAppBus().onTransactionAdded.onNext(it)
                                        getView()?.closeActivity()
                                    }
                                },
                                {
                                    getView()?.showError("Could not save!")
                                }
                        )
        )
    }

}