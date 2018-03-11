package io.github.amanshuraikwar.howmuch.data

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.amanshuraikwar.howmuch.data.local.LocalDataManager
import io.github.amanshuraikwar.howmuch.data.local.LocalDataManagerImpl
import io.github.amanshuraikwar.howmuch.data.local.room.AppDatabase
import io.github.amanshuraikwar.howmuch.di.ApplicationContext
import javax.inject.Singleton

/**
 * Created by amanshuraikwar on 07/03/18.
 */
@Module
abstract class DataManagerModule {

    @Singleton
    @Binds
    abstract fun dataManager(dataManager: DataManagerImpl): DataManager

    @Singleton
    @Binds
    abstract fun getLocalDataManager(impl: LocalDataManagerImpl): LocalDataManager
}