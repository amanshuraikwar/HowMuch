package io.github.amanshuraikwar.howmuch.data.local

import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.amanshuraikwar.howmuch.data.local.prefs.PrefsDataManager
import io.github.amanshuraikwar.howmuch.data.local.prefs.PrefsDataManagerImpl
import io.github.amanshuraikwar.howmuch.data.local.sqlite.room.AppDatabase
import io.github.amanshuraikwar.howmuch.data.local.sqlite.SqliteDataManager
import io.github.amanshuraikwar.howmuch.data.local.sqlite.room.RoomDataManagerImpl
import io.github.amanshuraikwar.howmuch.data.local.sqlite.room.SpreadsheetDao
import io.github.amanshuraikwar.howmuch.di.ApplicationContext
import javax.inject.Singleton

interface LocaDataManagerDi {

    @Suppress("unused")
    @Module
    abstract class LocalDataManagerModule {

        @Singleton
        @Binds
        abstract fun sdm(impl: RoomDataManagerImpl): SqliteDataManager

        @Singleton
        @Binds
        abstract fun pdm(impl: PrefsDataManagerImpl): PrefsDataManager

        @Singleton
        @Binds
        abstract fun ldm(impl: LocalDataManagerImpl): LocalDataManager
    }

    @Module
    class LocalDataManagerProvides {

        @Singleton
        @Provides
        fun getSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }

        @Singleton
        @Provides
        fun getAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room
                    .databaseBuilder(
                            context,
                            AppDatabase::class.java,
                            AppDatabase.DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build()
        }

        @Singleton
        @Provides
        fun getSpreadsheetDao(appDatabase: AppDatabase): SpreadsheetDao {
            return appDatabase.spreadsheetDao
        }
    }
}