package io.github.amanshuraikwar.howmuch.ui.signin

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.di.ActivityScope
import io.github.amanshuraikwar.howmuch.di.FragmentScope

class SignInDi {

    @Module abstract class SignInModule {

        @ActivityScope
        @Binds
        abstract fun prsntr(presenter: SignInPresenter): SignInContract.Presenter

        @FragmentScope
        @ContributesAndroidInjector
        internal abstract fun frgmnt(): SignInFragment
    }
}