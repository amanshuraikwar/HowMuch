package io.github.amanshuraikwar.howmuch.ui.expense

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.sheets.v4.SheetsScopes
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.amanshuraikwar.howmuch.di.ActivityContext
import io.github.amanshuraikwar.howmuch.di.ActivityScope

class ExpenseDi {

    @Module abstract class ModuleAct {

        @ActivityScope @Binds
        abstract fun prsntr(presenter: ExpensePresenter): ExpenseContract.Presenter

        @ActivityScope @Binds @ActivityContext
        abstract fun activity(activity: ExpenseActivity): AppCompatActivity
    }
}
