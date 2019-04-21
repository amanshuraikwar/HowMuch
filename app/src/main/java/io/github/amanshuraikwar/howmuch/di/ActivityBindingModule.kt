package io.github.amanshuraikwar.howmuch.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.di.GoogleSignInProvides
import io.github.amanshuraikwar.howmuch.ui.addexpense.AddExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.categories.CategoriesFragment
import io.github.amanshuraikwar.howmuch.ui.editcategories.EditCategoriesActivity
import io.github.amanshuraikwar.howmuch.ui.editwallets.EditWalletsActivity
import io.github.amanshuraikwar.howmuch.ui.expense.ExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.history.HistoryFragment
import io.github.amanshuraikwar.howmuch.ui.history.activity.HistoryActivity
import io.github.amanshuraikwar.howmuch.ui.home.HomeActivity
import io.github.amanshuraikwar.howmuch.ui.profile.ProfileFragment
import io.github.amanshuraikwar.howmuch.ui.settings.SettingsFragment
import io.github.amanshuraikwar.howmuch.ui.settings.activity.SettingsActivity
import io.github.amanshuraikwar.howmuch.ui.signin.SignInFragment
import io.github.amanshuraikwar.howmuch.ui.stats.StatsFragment
import io.github.amanshuraikwar.howmuch.ui.wallet.WalletActivity
import io.github.amanshuraikwar.howmuch.ui.wallets.WalletsFragment

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

    @ContributesAndroidInjector(
            modules = [WalletActivity.WalletModule::class]
    )
    abstract fun h(): WalletActivity

    @ContributesAndroidInjector(
            modules = [HistoryActivity.DiModule::class]
    )
    abstract fun i(): HistoryActivity

    @ContributesAndroidInjector(
            modules = [CategoriesFragment.DiModule::class]
    )
    abstract fun j(): CategoriesFragment

    @ContributesAndroidInjector(
            modules = [WalletsFragment.DiModule::class]
    )
    abstract fun k(): WalletsFragment

    @ContributesAndroidInjector(
            modules = [EditCategoriesActivity.DiModule::class]
    )
    abstract fun l(): EditCategoriesActivity

    @ContributesAndroidInjector(
            modules = [EditWalletsActivity.DiModule::class]
    )
    abstract fun m(): EditWalletsActivity

    @ContributesAndroidInjector(
            modules = [SettingsActivity.DiModule::class]
    )
    abstract fun n(): SettingsActivity

    @ContributesAndroidInjector(
            modules = [SettingsFragment.DiModule::class]
    )
    abstract fun o(): SettingsFragment
}