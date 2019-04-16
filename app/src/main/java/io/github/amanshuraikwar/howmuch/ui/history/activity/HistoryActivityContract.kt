package io.github.amanshuraikwar.howmuch.ui.history.activity

import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.base.util.Util
import javax.inject.Inject

interface HistoryActivityContract {

    interface View : BaseView {
        fun loadFragment()
    }

    interface Presenter : BasePresenter<View>

    class PresenterImpl @Inject constructor(appBus: AppBus,
                                            dataManager: DataManager)
        : BasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                init()
            }
        }

        private fun init() {

            getView()?.loadFragment()
        }
    }
}