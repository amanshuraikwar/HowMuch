package io.github.amanshuraikwar.howmuch.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.ui.history.HistoryDi
import io.github.amanshuraikwar.howmuch.ui.home.HomeActivity
import io.github.amanshuraikwar.howmuch.ui.home.HomeDi

/**
 * Created by Amanshu Raikwar on 07/03/18.
 */

@Module abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(
            modules = [
                (HomeDi.HomeModule::class),
                (HomeDi.HomeProvides::class),
                (HistoryDi.HistoryModule::class)])
    abstract fun home(): HomeActivity
}