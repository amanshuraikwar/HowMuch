package io.github.amanshuraikwar.howmuch

import android.content.SharedPreferences
import android.util.Log
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.local.sharedprefs.SharedPrefsKeys
import io.github.amanshuraikwar.howmuch.di.DaggerAppComponent
import io.github.amanshuraikwar.howmuch.util.LogUtil
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject

/**
 * Created by amanshuraikwar on 07/03/18.
 */
class MyApp : DaggerApplication() {

    private val TAG = LogUtil.getLogTag(this)

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>
            = DaggerAppComponent.builder().application(this).build()

    @Inject lateinit var appBus: AppBus
    @Inject lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        // catching undeliverable exceptions from rx-java
        RxJavaPlugins
                .setErrorHandler(
                        {
                            error ->
                            error.printStackTrace()
                            Log.d(TAG, error.message)
                        })

        sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }

    override fun onTerminate() {
        super.onTerminate()

        sharedPrefs.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }

    private var sharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener {
                _, key ->
                if (key == SharedPrefsKeys.KEY_DAILY_LIMIT_AMOUNT) {
                    Log.d(TAG, "sharedPrefsChanged")
                    appBus.onSharedPrefsChanged.onNext(key)
                }
            }
}