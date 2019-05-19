package io.github.amanshuraikwar.playground.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.di.GoogleSignInProvides
import io.github.amanshuraikwar.playground.ui.main.MainActivity

/**
 * Created by Amanshu Raikwar on 07/03/18.
 */

@Suppress("unused")
@Module abstract class ActivityBindingModule {

    @ContributesAndroidInjector(
            modules = [MainActivity.DiModule::class, GoogleSignInProvides::class]
    )
    abstract fun a(): MainActivity
}