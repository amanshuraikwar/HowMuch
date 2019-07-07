package io.github.amanshuraikwar.howmuch.ui.addexpense

import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import javax.inject.Inject

interface AddExpenseActivityContract {

    interface View : BaseView {
        fun loadFragment()
        fun close(success: Boolean)
    }

    interface Presenter : BasePresenter<View>

    class PresenterImpl @Inject constructor(appBus: AppBus,
                                            dataManager: DataManager)
        : HowMuchBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                init()
            }
        }

        private fun init() {

            getView()?.loadFragment()

            getAppBus()
                    .onAddExpenseInitFailed
                    .subscribe {
                        getView()?.close(false)
                    }
                    .addToCleanup()

            getAppBus()
                    .onAddExpenseProcessCompleted
                    .subscribe {
                        getView()?.close(true)
                    }
                    .addToCleanup()
        }
    }
}