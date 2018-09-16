package io.github.amanshuraikwar.howmuch.ui.stats

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.di.ActivityContext
import io.github.amanshuraikwar.howmuch.di.ActivityScope
import io.github.amanshuraikwar.howmuch.di.FragmentScope

class StatsDi {

    @Module abstract class StatsModule {

        @ActivityScope
        @Binds
        abstract fun prsntr(presenter: StatsPresenter): StatsContract.Presenter

        @FragmentScope
        @ContributesAndroidInjector
        internal abstract fun frgmnt(): StatsFragment
    }
}