package io.github.amanshuraikwar.howmuch.ui.settings

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.di.ActivityScope
import io.github.amanshuraikwar.howmuch.di.FragmentScope

class SettingsDi {

    @Module abstract class ModuleAct {

        @ActivityScope
        @Binds
        abstract fun prsntr(presenter: SettingsPresenter): SettingsContract.Presenter

        @FragmentScope
        @ContributesAndroidInjector
        internal abstract fun frgmnt(): SettingsFragment
    }
}