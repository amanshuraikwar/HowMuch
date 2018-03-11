package io.github.amanshuraikwar.howmuch.ui.intro

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.util.LogUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by amanshuraikwar on 09/03/18.
 */
class IntroPresenter @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<IntroContract.View>(appBus, dataManager), IntroContract.Presenter {

    private val TAG = LogUtil.getLogTag(this)

    private var disposables: Set<Disposable> = mutableSetOf()

    override fun onDoneClick(amount: String) {

        with(amount.trim()) {
            if (this == "" || this == "0") {
                getView()?.showError("Please enter a valid daily limit amount!")
            } else {
                saveDailyLimit(this.toInt())
            }
        }
    }

    private fun saveDailyLimit(amount: Int) {

        disposables.plusElement(
                getDataManager()
                        .setDailyLimitAmount(amount)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    getView()?.startHomeActivity()
                                },
                                {
                                    getView()?.showError("Could not save!")
                                }
                        )
        )
    }

    override fun onDetach() {
        super.onDetach()

        for (disposable in disposables) {
            if (! disposable.isDisposed) {
                disposable.dispose()
            }
        }
    }
}