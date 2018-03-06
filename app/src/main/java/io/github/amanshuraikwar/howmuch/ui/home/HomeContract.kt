package io.github.amanshuraikwar.howmuch.ui.home

import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

/**
 * Created by amanshuraikwar on 07/03/18.
 */
interface HomeContract {

    interface View : BaseView

    interface Presenter : BasePresenter<View>
}