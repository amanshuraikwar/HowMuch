package io.github.amanshuraikwar.playground.ui

import io.github.amanshuraikwar.howmuch.base.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseView
import io.github.amanshuraikwar.playground.data.DataManager
import io.github.amanshuraikwar.playground.data.PlaygroundBus

abstract class PlaygroundBasePresenterImpl<V: BaseView>
constructor(appBus: PlaygroundBus,
            dataManager: DataManager)
    : BasePresenterImpl<V, PlaygroundBus, DataManager>(appBus, dataManager)