package io.github.amanshuraikwar.howmuch.ui.createsheet

import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

interface CreateSheetContract {

    interface View : BaseView

    interface Presenter : BasePresenter<View>
}