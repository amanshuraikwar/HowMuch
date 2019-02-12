package io.github.amanshuraikwar.howmuch.ui.onboarding.activity

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.base.BaseView
import io.github.amanshuraikwar.howmuch.ui.onboarding.OnboardingScreen
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface OnboardingContract {

    interface View : BaseView {
        fun close()
        fun startHomeActivity()
    }

    interface Presenter : BasePresenter<View>

    class OnboardingPresenter
    @Inject constructor(appBus: AppBus, dataManager: DataManager)
        : BasePresenterImpl<View>(appBus, dataManager), Presenter {

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                init()
            }
        }

        private fun init() {
            getAppBus()
                    .onBoardingScreenState
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {

                        state ->

                        if (state == OnboardingScreen.State.ONBOARDING_COMPLETE) {
                            getView()?.run {
                                startHomeActivity()
                                close()
                            }
                        }
                    }
                    .addToCleanup()
        }
    }

}