package io.github.amanshuraikwar.howmuch.ui.home

import android.util.Log
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.util.Util
import javax.inject.Inject
import kotlin.math.log

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

        getView()?.loadPage(NavigationPage.ADD_EXPENSE)
    }
    //endregion

    override fun onNavigationItemSelected(itemId: Int) {
        Log.d(TAG, "onNavigationItemSelected:called")
        Log.i(TAG, "onNavigationItemSelected: itemId = $itemId")
        when(itemId) {
            R.id.navigation_home -> getView()?.loadPage(NavigationPage.ADD_EXPENSE)
            R.id.navigation_history -> getView()?.loadPage(NavigationPage.HISTORY)
            R.id.navigation_stats -> getView()?.loadPage(NavigationPage.STATS)
        }
    }
}