package io.github.amanshuraikwar.howmuch.ui.onboarding

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OnboardingPresenter @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<OnboardingContract.View>(appBus, dataManager), OnboardingContract.Presenter {

    private val TAG = Util.getTag(this)

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
                    @Suppress("NON_EXHAUSTIVE_WHEN")
                    when(it) {
                        OnboardingScreen.State.SIGN_IN_COMPLETE -> getView()?.setCurPage(1)
                        OnboardingScreen.State.CREATE_SHEET_COMPLETE ->
                            getAppBus().onBoardingScreenState.onNext(
                                    OnboardingScreen.State.ONBOARDING_COMPLETE)
                    }
                }
    }
}