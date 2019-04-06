package io.github.amanshuraikwar.howmuch.ui.home

import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.ui.base.*
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
        : BasePresenterImpl<View>(appBus, dataManager), Presenter {

        override fun onAttach(wasViewRecreated: Boolean) {

            super.onAttach(wasViewRecreated)

            if (wasViewRecreated) {
                attachToAppBus()
                init()
            }
        }

        private fun init() {

            getDataManager()
                    .isSignedIn()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        signedIn ->
                        if (signedIn) {
                            getView()?.run {
                                loadHistoryFragment()
                                showAddTransactionBtn()
                                showBnv()
                            }
                        } else {
                            getView()?.loadSignInFragment()
                        }
                    }
                    .addToCleanup()
        }

        private fun attachToAppBus() {

            getAppBus()
                    .onSignInSuccessful
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
                    .onSignOut
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        getDataManager().signOut().subscribe().addToCleanup()
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