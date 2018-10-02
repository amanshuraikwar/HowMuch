package io.github.amanshuraikwar.howmuch.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import io.github.amanshuraikwar.howmuch.MyApp
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.bus.AppBusModule
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.DataManagerModule
import io.github.amanshuraikwar.howmuch.data.DataManagerModuleProvides
import io.github.amanshuraikwar.howmuch.data.local.LocaDataManagerDi
import io.github.amanshuraikwar.howmuch.data.network.NetworkDataManagerModule
import io.github.amanshuraikwar.howmuch.data.network.NetworkManagerProvides
import javax.inject.Singleton

/**
 * Created by Amanshu Raikwar on 07/03/18.
 */
@Singleton
@Component(
        modules = [
            (AppModule::class),
            (NetworkDataManagerModule::class),
            (NetworkManagerProvides::class),
            (LocaDataManagerDi.LocalDataManagerModule::class),
            (LocaDataManagerDi.LocalDataManagerProvides::class),
            (DataManagerModule::class),
            (DataManagerModuleProvides::class),
            (AppBusModule::class),
            (ActivityBindingModule::class),
            (AndroidSupportInjectionModule::class)])
interface AppComponent : AndroidInjector<MyApp> {

    // abstract functions to get data manager and app bus from app component
    // this has to be explicitly specified otherwise
    // app component will not file instances from dependent components
    fun dataManager(): DataManager
    fun appBus(): AppBus

    @Component.Builder interface Builder {

        @BindsInstance fun application(app: Application): Builder
        fun build(): AppComponent
    }
}