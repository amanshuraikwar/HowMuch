package io.github.amanshuraikwar.howmuch.ui

import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseView

abstract class HowMuchBasePresenterImpl<V: BaseView>
constructor(appBus: AppBus,
            dataManager: DataManager)
    : BasePresenterImpl<V, AppBus, DataManager>(appBus, dataManager)