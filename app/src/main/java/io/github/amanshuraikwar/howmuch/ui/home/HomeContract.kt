package io.github.amanshuraikwar.howmuch.ui.home

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.ui.base.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface HomeContract {

    interface View : BaseView {
        fun close()
        fun loadSignInFragment()
        fun loadHistoryFragment()
        fun showAddTransactionBtn()
        fun hideAddTransactionBtn()
        fun showBnv()
        fun hideBnv()
    }

    interface Presenter : BasePresenter<View> {
        fun onTransactionAdded()
    }

    class HomePresenter
    @Inject constructor(appBus: AppBus,
                        dataManager: DataManager)
        : AccountPresenter<View>(appBus, dataManager), Presenter {

        override fun onAttach(wasViewRecreated: Boolean) {

            super.onAttach(wasViewRecreated)

            if (wasViewRecreated) {
                attachToAppBus()
                init()
            }
        }

        private fun init() {

            getDataManager()
                    .isInitialOnboardingDone()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        done ->
                        if (!done) {
                            getView()?.loadSignInFragment()
                        } else {
                            getView()?.run {
                                loadHistoryFragment()
                                showAddTransactionBtn()
                                showBnv()
                            }
                        }
                    }
                    .addToCleanup()
        }

        private fun attachToAppBus() {

            getAppBus()
                    .onBoardingComplete
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        getView()?.run {
                            loadHistoryFragment()
                            showAddTransactionBtn()
                            showBnv()
                        }
                    }
                    .addToCleanup()

            getAppBus()
                    .onLogout
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        getDataManager().setInitialOnboardingDone(false).subscribe().addToCleanup()
                        getView()?.run {
                            hideAddTransactionBtn()
                            hideBnv()
                            loadSignInFragment()
                        }
                    }
                    .addToCleanup()
        }

        override fun onTransactionAdded() {
            getAppBus().onTransactionAdded.onNext(Any())
        }
    }
}