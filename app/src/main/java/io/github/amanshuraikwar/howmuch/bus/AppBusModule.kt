package io.github.amanshuraikwar.howmuch.bus

import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.DataManagerImpl
import javax.inject.Singleton

/**
 * Created by amanshuraikwar on 07/03/18.
 */
@Module class AppBusModule {

    @Singleton @Provides fun appBus(): AppBus = AppBus()
}