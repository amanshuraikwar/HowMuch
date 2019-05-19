package io.github.amanshuraikwar.playground.di

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import io.github.amanshuraikwar.howmuch.base.di.ApplicationContext
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.di.GoogleSheetsDataManagerModule
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.di.GoogleSheetsDataManagerProvides
import io.github.amanshuraikwar.playground.MyApp
import io.github.amanshuraikwar.playground.data.DataManager
import io.github.amanshuraikwar.playground.data.PlaygroundBus
import io.github.amanshuraikwar.playground.data.PlaygroundBusModule
import javax.inject.Singleton

/**
 * Created by Amanshu Raikwar on 07/03/18.
 */
@Singleton
@Component(
        modules = [
            AppModule::class,
            GoogleSheetsDataManagerModule::class,
            GoogleSheetsDataManagerProvides::class,
            DataManagerModule::class,
            PlaygroundBusModule::class,
            ActivityBindingModule::class,
            AndroidSupportInjectionModule::class])
interface AppComponent : AndroidInjector<MyApp> {

    // abstract functions to get data manager and app bus from app component
    // this has to be explicitly specified otherwise
    // app component will not file instances from dependent components
    fun a(): DataManager
    fun b(): PlaygroundBus
    @ApplicationContext fun c(): Context

    @Component.Builder interface Builder {

        @BindsInstance fun application(app: Application): Builder
        fun build(): AppComponent
    }
}