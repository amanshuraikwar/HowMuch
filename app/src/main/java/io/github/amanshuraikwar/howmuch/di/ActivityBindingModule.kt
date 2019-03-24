package io.github.amanshuraikwar.howmuch.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.di.modules.GoogleSignInProvides
import io.github.amanshuraikwar.howmuch.ui.addexpense.AddExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.expense.ExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.history.HistoryFragment
import io.github.amanshuraikwar.howmuch.ui.home.HomeActivity
import io.github.amanshuraikwar.howmuch.ui.signin.SignInFragment
import io.github.amanshuraikwar.howmuch.ui.stats.StatsFragment

/**
 * Created by Amanshu Raikwar on 07/03/18.
 */

@Module abstract class ActivityBindingModule {

    @ContributesAndroidInjector(
            modules = [HomeActivity.HomeModule::class, GoogleSignInProvides::class]
    )
    abstract fun home(): HomeActivity

    @ContributesAndroidInjector(
            modules = [AddExpenseActivity.AddTransactionModule::class, GoogleSignInProvides::class]
    )
    abstract fun addExpense(): AddExpenseActivity

    @ContributesAndroidInjector(
            modules = [ExpenseActivity.ExpenseModule::class, GoogleSignInProvides::class]
    )
    abstract fun expense(): ExpenseActivity

    @ContributesAndroidInjector(
            modules = [SignInFragment.SignInModule::class]
    )
    internal abstract fun signInFragment(): SignInFragment

    @ContributesAndroidInjector(
            modules = [HistoryFragment.HistoryModule::class]
    )
    internal abstract fun historyFragment(): HistoryFragment

    @ContributesAndroidInjector(
            modules = [StatsFragment.StatsModule::class]
    )
    internal abstract fun statsFragment(): StatsFragment
}