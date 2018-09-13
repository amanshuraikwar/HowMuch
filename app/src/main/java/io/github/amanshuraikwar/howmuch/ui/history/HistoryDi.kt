package io.github.amanshuraikwar.howmuch.ui.history

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.di.ActivityContext
import io.github.amanshuraikwar.howmuch.di.ActivityScope
import io.github.amanshuraikwar.howmuch.di.FragmentScope

class HistoryDi {

    @Module abstract class HistoryModule {

        @ActivityScope
        @Binds
        abstract fun prsntr(presenter: HistoryPresenter): HistoryContract.Presenter

        @FragmentScope
        @ContributesAndroidInjector
        internal abstract fun HistoryFragment(): HistoryFragment
    }
}