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
import javax.inject.Singleton

/**
 * Created by amanshuraikwar on 07/03/18.
 */
@Singleton
@Component(
        modules = arrayOf(
                AppModule::class,
                DataManagerModule::class,
                AppBusModule::class,
                ActivityBindingModule::class,
                AndroidSupportInjectionModule::class))
interface AppComponent : AndroidInjector<MyApp> {

    fun dataManager(): DataManager
    fun appBus(): AppBus

    @Component.Builder interface Builder {

        @BindsInstance fun application(app: Application): AppComponent.Builder
        fun build(): AppComponent
    }
}