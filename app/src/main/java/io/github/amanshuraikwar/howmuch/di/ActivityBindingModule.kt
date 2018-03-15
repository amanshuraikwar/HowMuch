package io.github.amanshuraikwar.howmuch.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.ui.addtransaction.AddTransactionActivity
import io.github.amanshuraikwar.howmuch.ui.addtransaction.AddTransactionModule
import io.github.amanshuraikwar.howmuch.ui.expenseday.ExpenseDayActivity
import io.github.amanshuraikwar.howmuch.ui.expenseday.ExpenseDayModule
import io.github.amanshuraikwar.howmuch.ui.home.HomeActivity
import io.github.amanshuraikwar.howmuch.ui.home.HomeModule
import io.github.amanshuraikwar.howmuch.ui.intro.IntroActivity
import io.github.amanshuraikwar.howmuch.ui.intro.IntroModule

/**
 * Created by amanshuraikwar on 07/03/18.
 */

@Module abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(HomeModule::class))
    abstract fun homeActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(IntroModule::class))
    abstract fun introActivity(): IntroActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(AddTransactionModule::class))
    abstract fun addTransactionActivity(): AddTransactionActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(ExpenseDayModule::class))
    abstract fun expenseDayActivity(): ExpenseDayActivity
}