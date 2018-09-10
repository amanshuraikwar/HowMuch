package io.github.amanshuraikwar.howmuch.bus

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger Module to provide Event Bus related instances.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 07/03/18.
 */
@Module class AppBusModule {

    @Singleton @Provides fun appBus(): AppBus = AppBus()
}