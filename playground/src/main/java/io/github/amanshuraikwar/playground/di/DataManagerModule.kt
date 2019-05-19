package io.github.amanshuraikwar.playground.di

import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.playground.data.DataManager
import io.github.amanshuraikwar.playground.data.DataManagerImpl
import javax.inject.Singleton

/**
 * Dagger Module to provide Data Manager related instances.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 07/03/18.
 */
@Module
abstract class DataManagerModule {

    @Singleton
    @Binds
    abstract fun dataManager(dataManager: DataManagerImpl): DataManager
}