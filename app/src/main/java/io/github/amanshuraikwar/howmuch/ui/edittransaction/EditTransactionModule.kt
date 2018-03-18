package io.github.amanshuraikwar.howmuch.ui.edittransaction

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.di.ActivityContext
import io.github.amanshuraikwar.howmuch.di.ActivityScope

/**
 * Created by amanshuraikwar on 07/03/18.
 */

@Module abstract class EditTransactionModule {

    @ActivityScope @Binds
    abstract fun prsntr (presenter: EditTransactionPresenter): EditTransactionContract.Presenter

    @ActivityScope @Binds @ActivityContext
    abstract fun activity (activity: AppCompatActivity): AppCompatActivity
}