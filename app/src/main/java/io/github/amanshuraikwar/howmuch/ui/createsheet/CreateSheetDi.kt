package io.github.amanshuraikwar.howmuch.ui.createsheet

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.di.ActivityScope
import io.github.amanshuraikwar.howmuch.di.FragmentScope

class CreateSheetDi {

    @Module abstract class ModuleAct {

        @ActivityScope
        @Binds
        abstract fun prsntr(presenter: CreateSheetPresenter): CreateSheetContract.Presenter

        @FragmentScope
        @ContributesAndroidInjector
        internal abstract fun frgmnt(): CreateSheetFragment
    }
}