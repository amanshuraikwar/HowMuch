package io.github.amanshuraikwar.howmuch.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.ui.home.HomeActivity
import io.github.amanshuraikwar.howmuch.ui.home.HomeModule

/**
 * Created by amanshuraikwar on 07/03/18.
 */

@Module abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(HomeModule::class))
    abstract fun homeActivity(): HomeActivity
}