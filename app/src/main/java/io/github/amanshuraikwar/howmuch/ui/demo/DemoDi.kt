package io.github.amanshuraikwar.howmuch.ui.demo

import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.sheets.v4.SheetsScopes
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.amanshuraikwar.howmuch.di.ActivityContext
import io.github.amanshuraikwar.howmuch.di.ActivityScope

/**
 * Dagger Module to provide HomeActivity related instances.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */

class DemoDi {

    @Module abstract class DemoModule {

        @ActivityScope @Binds
        abstract fun prsntr(presenter: DemoPresenter): DemoContract.Presenter

        @ActivityScope @Binds @ActivityContext
        abstract fun activity(activity: DemoActivity): AppCompatActivity
    }
}
