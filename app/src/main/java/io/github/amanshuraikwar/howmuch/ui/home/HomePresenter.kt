package io.github.amanshuraikwar.howmuch.ui.home

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.util.LogUtil
import javax.inject.Inject

/**
 * Created by amanshuraikwar on 07/03/18.
 */
class HomePresenter @Inject constructor(appBus: AppBus, dataManager: DataManager)
    : BasePresenterImpl<HomeContract.View>(appBus, dataManager), HomeContract.Presenter {

    private val TAG = LogUtil.getLogTag(this)
}