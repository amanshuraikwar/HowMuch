package io.github.amanshuraikwar.howmuch.ui.settings.activity

import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import javax.inject.Inject

interface SettingsActivityContract {

    interface View : BaseView {
        fun loadSettings()
    }

    interface Presenter : BasePresenter<View>

    class PresenterImpl @Inject constructor(appBus: AppBus,
                                            dataManager: DataManager)
        : BasePresenterImpl<View>(appBus, dataManager), Presenter {

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                init()
            }
        }

        private fun init() {
            getView()?.loadSettings()
        }
    }
}