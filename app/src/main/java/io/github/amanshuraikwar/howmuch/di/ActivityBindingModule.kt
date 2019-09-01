package io.github.amanshuraikwar.howmuch.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.di.GoogleSignInProvides
import io.github.amanshuraikwar.howmuch.ui.about.AboutActivity
import io.github.amanshuraikwar.howmuch.ui.addexpense.AddExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.addexpense.AddExpenseFragment
import io.github.amanshuraikwar.howmuch.ui.categories.CategoriesFragment
import io.github.amanshuraikwar.howmuch.ui.category.CategoryActivity
import io.github.amanshuraikwar.howmuch.ui.expense.ExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.history.HistoryFragment
import io.github.amanshuraikwar.howmuch.ui.history.HistoryActivity
import io.github.amanshuraikwar.howmuch.ui.home.HomeActivity
import io.github.amanshuraikwar.howmuch.ui.monthlybudget.MonthlyBudgetActivity
import io.github.amanshuraikwar.howmuch.ui.profile.ProfileFragment
import io.github.amanshuraikwar.howmuch.ui.search.SearchActivity
import io.github.amanshuraikwar.howmuch.ui.signin.SignInFragment
import io.github.amanshuraikwar.howmuch.ui.stats.StatsFragment

/**
 * Created by Amanshu Raikwar on 07/03/18.
 */

@Suppress("unused")
@Module abstract class ActivityBindingModule {

    @ContributesAndroidInjector(
            modules = [HomeActivity.HomeModule::class]
    )
    abstract fun a(): HomeActivity

    @ContributesAndroidInjector(
            modules = [AddExpenseFragment.DiModule::class]
    )
    abstract fun b(): AddExpenseFragment

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

    @ContributesAndroidInjector(
            modules = [HistoryActivity.DiModule::class]
    )
    abstract fun i(): HistoryActivity

    @ContributesAndroidInjector(
            modules = [CategoriesFragment.DiModule::class]
    )
    abstract fun j(): CategoriesFragment

    @ContributesAndroidInjector(
            modules = [AboutActivity.DiModule::class]
    )
    abstract fun p(): AboutActivity

    @ContributesAndroidInjector(
            modules = [SearchActivity.DiModule::class]
    )
    abstract fun q(): SearchActivity

    @ContributesAndroidInjector(
            modules = [MonthlyBudgetActivity.DiModule::class]
    )
    abstract fun r(): MonthlyBudgetActivity

    @ContributesAndroidInjector(
            modules = [CategoryActivity.DiModule::class]
    )
    abstract fun s(): CategoryActivity

    @ContributesAndroidInjector(
            modules = [AddExpenseActivity.DiModule::class]
    )
    abstract fun t(): AddExpenseActivity
}