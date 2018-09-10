package io.github.amanshuraikwar.howmuch.ui.home

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.di.ActivityContext
import io.github.amanshuraikwar.howmuch.di.ActivityScope

/**
 * Dagger Module to provide HomeActivity related instances.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */
@Module abstract class HomeModule {

    @ActivityScope @Binds
    abstract fun prsntr(presenter: HomePresenter): HomeContract.Presenter

    @ActivityScope @Binds @ActivityContext
    abstract fun activity(activity: AppCompatActivity): AppCompatActivity
}