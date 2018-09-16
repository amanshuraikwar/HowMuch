package io.github.amanshuraikwar.howmuch.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.di.modules.GoogleSignInProvides
import io.github.amanshuraikwar.howmuch.ui.addexpense.AddExpenseDi
import io.github.amanshuraikwar.howmuch.ui.history.HistoryDi
import io.github.amanshuraikwar.howmuch.ui.home.HomeActivity
import io.github.amanshuraikwar.howmuch.ui.home.HomeDi
import io.github.amanshuraikwar.howmuch.ui.signin.SignInDi
import io.github.amanshuraikwar.howmuch.ui.stats.StatsDi

/**
 * Created by Amanshu Raikwar on 07/03/18.
 */

@Module abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(
            modules = [
                (HomeDi.HomeModule::class),
                (GoogleSignInProvides::class),
                (HistoryDi.HistoryModule::class),
                (AddExpenseDi.AddExpenseModule::class),
                (StatsDi.StatsModule::class),
                (SignInDi.SignInModule::class)])
    abstract fun home(): HomeActivity
}