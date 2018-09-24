package io.github.amanshuraikwar.howmuch.ui.demo

import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

interface DemoContract {

    interface View : BaseView

    interface Presenter : BasePresenter<View>
}