package io.github.amanshuraikwar.howmuch.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.base.di.ApplicationContext

/**
 * Created by Amanshu Raikwar on 07/03/18.
 */
@Module abstract class AppModule {

    @Binds @ApplicationContext
    abstract fun bindContext(app: Application): Context
}