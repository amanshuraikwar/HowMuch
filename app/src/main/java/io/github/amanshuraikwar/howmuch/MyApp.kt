package io.github.amanshuraikwar.howmuch

import android.util.Log
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.github.amanshuraikwar.howmuch.di.DaggerAppComponent
import io.github.amanshuraikwar.howmuch.util.LogUtil
import io.reactivex.plugins.RxJavaPlugins

/**
 * Created by amanshuraikwar on 07/03/18.
 */
class MyApp : DaggerApplication() {

    private val TAG = LogUtil.getLogTag(this)

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>
            = DaggerAppComponent.builder().application(this).build()

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
    }
}