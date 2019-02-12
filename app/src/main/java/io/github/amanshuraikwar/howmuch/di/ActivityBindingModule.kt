package io.github.amanshuraikwar.howmuch.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.di.modules.GoogleSignInProvides
import io.github.amanshuraikwar.howmuch.ui.addexpense.activity.AddExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.demo.DemoActivity
import io.github.amanshuraikwar.howmuch.ui.demo.DemoDi
import io.github.amanshuraikwar.howmuch.ui.expense.ExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.history.HistoryFragment
import io.github.amanshuraikwar.howmuch.ui.home.HomeActivity
import io.github.amanshuraikwar.howmuch.ui.onboarding.activity.OnboardingActivity
import io.github.amanshuraikwar.howmuch.ui.signin.SignInFragment

/**
 * Created by Amanshu Raikwar on 07/03/18.
 */

@Module abstract class ActivityBindingModule {

    @ContributesAndroidInjector(
            modules = [HomeActivity.HomeModule::class, GoogleSignInProvides::class]
    )
    abstract fun home(): HomeActivity

    @ContributesAndroidInjector(
            modules = [OnboardingActivity.OnboardingModule::class, GoogleSignInProvides::class]
    )
    abstract fun onboarding(): OnboardingActivity

    @ContributesAndroidInjector(
            modules = [
                (DemoDi.DemoModule::class),
                (GoogleSignInProvides::class)])
    abstract fun demo(): DemoActivity

    @ContributesAndroidInjector(
            modules = [AddExpenseActivity.AddTransactionModule::class, GoogleSignInProvides::class]
    )
    abstract fun addTransaction(): AddExpenseActivity

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
}