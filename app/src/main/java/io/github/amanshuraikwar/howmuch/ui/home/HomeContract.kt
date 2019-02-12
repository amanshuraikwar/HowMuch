package io.github.amanshuraikwar.howmuch.ui.home

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.ui.base.*
import io.github.amanshuraikwar.howmuch.ui.onboarding.OnboardingScreen
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * MVP contract of HomeActivity.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */
interface HomeContract {

    enum class NavigationPage {
        ADD_EXPENSE, HISTORY, STATS, SIGN_IN, ONBOARDING, SETTINGS
    }

    interface View : BaseView, LoadingView, UiMessageView, GoogleAccountView {
        fun startOnboardingActivity()
        fun close()
        fun loadSignInFragment()
        fun loadHistoryFragment()
        fun showAddTransactionBtn()
    }

    interface Presenter : BasePresenter<View>

    class HomePresenter
    @Inject constructor(appBus: AppBus,
                        dataManager: DataManager)
        : BasePresenterImpl<View>(appBus, dataManager), Presenter {

        override fun onAttach(wasViewRecreated: Boolean) {

            super.onAttach(wasViewRecreated)

            if (wasViewRecreated) {
                init()
            }
        }

        private fun init() {
            getView()?.loadSignInFragment()
            attachToAppBus()
        }

        private fun attachToAppBus() {

            getAppBus()
                    .onBoardingScreenState
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it == OnboardingScreen.State.ONBOARDING_COMPLETE) {
                            getView()?.run {
                                loadHistoryFragment()
                                showAddTransactionBtn()
                            }
                        }
                    }
                    .addToCleanup()

        }
    }
}