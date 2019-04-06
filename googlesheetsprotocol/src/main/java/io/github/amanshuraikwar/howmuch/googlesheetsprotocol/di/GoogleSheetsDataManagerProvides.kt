package io.github.amanshuraikwar.howmuch.googlesheetsprotocol.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import dagger.Module
import dagger.Provides
import io.github.amanshuraikwar.howmuch.base.di.ApplicationContext
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.room.AppDatabase
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.room.spreadsheet.SpreadsheetDao

@Module
class GoogleSheetsDataManagerProvides {

    @Provides
    fun a() = AndroidHttp.newCompatibleTransport()

    @Provides
    fun b(): JsonFactory = JacksonFactory.getDefaultInstance()

    @Provides
    fun c(@ApplicationContext context: Context): AppDatabase {
        return Room
                .databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        AppDatabase.DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .build()
    }

    @Provides
    fun d(appDatabase: AppDatabase): SpreadsheetDao {
        return appDatabase.spreadsheetDao
    }

    @Provides
    fun e(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}