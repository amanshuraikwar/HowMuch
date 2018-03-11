package io.github.amanshuraikwar.howmuch.data

import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.data.local.room.AppDatabase
import io.github.amanshuraikwar.howmuch.di.ApplicationContext
import javax.inject.Singleton

/**
 * Created by amanshuraikwar on 09/03/18.
 */
@Module class DateManagerModuleProvides {

    @Singleton
    @Provides
    fun getAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "howmuch-database")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun sharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context
                .getSharedPreferences(
                        context.getString(R.string.preference_file_key),
                        Context.MODE_PRIVATE)
    }
}