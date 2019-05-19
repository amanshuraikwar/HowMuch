package io.github.amanshuraikwar.playground;

import android.util.Log;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.github.amanshuraikwar.howmuch.base.util.Util;
import io.github.amanshuraikwar.playground.di.DaggerAppComponent;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Application Class.
 *
 * @author Amanshu Raikwar
 * Created by amanshuraikwar on 18/12/17.
 */
public class MyApp extends DaggerApplication {

    private final String TAG = Util.getTag(this);

    @Override
    public void onCreate() {
        super.onCreate();
        RxJavaPlugins.setErrorHandler(
                throwable -> {
                    throwable.printStackTrace();
                    Log.w(TAG, "onCreate: RxJavaPlugins", throwable);
                }
        );
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
