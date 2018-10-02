package io.github.amanshuraikwar.howmuch.ui.home

import android.util.Log
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.onboarding.OnboardingScreen
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Implementation of presenter of HomeActivity.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */
class HomePresenter @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<HomeContract.View>(appBus, dataManager), HomeContract.Presenter {

    private val TAG = Util.getTag(this)

    //region parent class methods
    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        if (wasViewRecreated) {
            init()
        }
    }
    //endregion

    private fun init() {

        getDataManager()
                .isInitialOnboardingDone()
                .doOnNext {
                    done ->
                    if (!done) {
                        getView()?.loadPage(NavigationPage.ONBOARDING)
                    }
                }
                .filter { done -> done }
                .subscribe {
                    getView()?.loadPage(NavigationPage.ADD_EXPENSE)
                    getView()?.showBottomNav()
                }

        getAppBus()
                .onBoardingScreenState
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    @Suppress("NON_EXHAUSTIVE_WHEN")
                    when(it) {
                        OnboardingScreen.State.ONBOARDING_COMPLETE -> {
                            getView()?.loadPage(NavigationPage.ADD_EXPENSE)
                            getView()?.showBottomNav()
                        }
                    }
                }
    }

    override fun onNavigationItemSelected(position: Int) {
        Log.d(TAG, "onNavigationItemSelected:called")
        Log.i(TAG, "onNavigationItemSelected: position = $position")
        when(position) {
            0 -> getView()?.loadPage(NavigationPage.ADD_EXPENSE)
            1 -> getView()?.loadPage(NavigationPage.HISTORY)
            2 -> getView()?.loadPage(NavigationPage.STATS)
        }
    }
}