package io.github.amanshuraikwar.howmuch.ui.onboarding

import io.github.amanshuraikwar.howmuch.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.ui.base.BaseView

interface OnboardingContract {

    interface View : BaseView {
        fun setCurPage(position: Int)
    }

    interface Presenter : BasePresenter<View>
}