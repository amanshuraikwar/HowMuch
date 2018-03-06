package io.github.amanshuraikwar.howmuch

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.github.amanshuraikwar.howmuch.di.DaggerAppComponent

/**
 * Created by amanshuraikwar on 07/03/18.
 */
class MyApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>
            = DaggerAppComponent.builder().application(this).build()
}