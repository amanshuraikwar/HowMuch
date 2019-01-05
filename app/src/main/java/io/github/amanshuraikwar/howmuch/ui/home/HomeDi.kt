package io.github.amanshuraikwar.howmuch.ui.home

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

/**
 * Dagger Module to provide HomeActivity related instances.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */

class HomeDi {

    @Module abstract class HomeModule {

        @ActivityScope @Binds
        abstract fun prsntr(presenter: HomePresenter): HomeContract.Presenter

        @ActivityScope @Binds @ActivityContext
        abstract fun activity(activity: HomeActivity): AppCompatActivity
    }
}
