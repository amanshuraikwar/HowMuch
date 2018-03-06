package io.github.amanshuraikwar.howmuch.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

/**
 * Created by amanshuraikwar on 07/03/18.
 */
@Module abstract class AppModule {

    @Binds @ApplicationContext abstract fun bindContext(app: Application): Context
}