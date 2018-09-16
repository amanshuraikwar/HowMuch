package io.github.amanshuraikwar.howmuch.ui.addexpense

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.di.ActivityScope
import io.github.amanshuraikwar.howmuch.di.FragmentScope

class AddExpenseDi {

    @Module abstract class AddExpenseModule {

        @ActivityScope
        @Binds
        abstract fun prsntr(presenter: AddExpensePresenter): AddExpenseContract.Presenter

        @FragmentScope
        @ContributesAndroidInjector
        internal abstract fun frgmt(): AddExpenseFragment
    }
}