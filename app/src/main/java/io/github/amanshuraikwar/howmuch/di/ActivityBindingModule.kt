package io.github.amanshuraikwar.howmuch.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.di.GoogleSignInProvides
import io.github.amanshuraikwar.howmuch.ui.addexpense.AddExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.expense.ExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.history.HistoryFragment
import io.github.amanshuraikwar.howmuch.ui.home.HomeActivity
import io.github.amanshuraikwar.howmuch.ui.profile.ProfileFragment
import io.github.amanshuraikwar.howmuch.ui.signin.SignInFragment
import io.github.amanshuraikwar.howmuch.ui.stats.StatsFragment

/**
 * Created by Amanshu Raikwar on 07/03/18.
 */

@Module abstract class ActivityBindingModule {

    @ContributesAndroidInjector(
            modules = [HomeActivity.HomeModule::class]
    )
    abstract fun a(): HomeActivity

    @ContributesAndroidInjector(
            modules = [AddExpenseActivity.AddTransactionModule::class]
    )
    abstract fun b(): AddExpenseActivity

    @ContributesAndroidInjector(
            modules = [ExpenseActivity.ExpenseModule::class]
    )
    abstract fun c(): ExpenseActivity

    @ContributesAndroidInjector(
            modules = [
                SignInFragment.DiModule::class,
                GoogleSignInProvides::class,
                SignInFragment.DiProvides::class
            ]
    )
    internal abstract fun d(): SignInFragment

    @ContributesAndroidInjector(
            modules = [HistoryFragment.HistoryModule::class]
    )
    internal abstract fun e(): HistoryFragment

    @ContributesAndroidInjector(
            modules = [StatsFragment.StatsModule::class]
    )
    internal abstract fun f(): StatsFragment

    @ContributesAndroidInjector(
            modules = [
                ProfileFragment.DiModule::class,
                GoogleSignInProvides::class,
                ProfileFragment.DiProvides::class
            ]
    )
    internal abstract fun g(): ProfileFragment
}