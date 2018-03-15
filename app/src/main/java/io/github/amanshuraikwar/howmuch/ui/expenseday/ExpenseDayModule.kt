package io.github.amanshuraikwar.howmuch.ui.expenseday

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.di.ActivityContext
import io.github.amanshuraikwar.howmuch.di.ActivityScope

/**
 * Created by amanshuraikwar on 12/03/18.
 */
@Module
abstract class ExpenseDayModule {

    @ActivityScope
    @Binds
    abstract fun expenseDayPrsntr(presenter: ExpenseDayPresenter): ExpenseDayContract.Presenter

    @ActivityScope
    @Binds
    @ActivityContext
    abstract fun activity(activity: AppCompatActivity): AppCompatActivity
}