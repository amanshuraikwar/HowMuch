package io.github.amanshuraikwar.howmuch.ui.onboarding

import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.sheets.v4.SheetsScopes
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.github.amanshuraikwar.howmuch.di.ActivityContext
import io.github.amanshuraikwar.howmuch.di.ActivityScope
import io.github.amanshuraikwar.howmuch.di.FragmentScope

/**
 * Dagger Module to provide HomeActivity related instances.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */

class OnboardingDi {

    @Module
    abstract class ModuleAct {

        @ActivityScope @Binds
        abstract fun prsntr(presenter: OnboardingPresenter): OnboardingContract.Presenter

        @FragmentScope
        @ContributesAndroidInjector
        internal abstract fun frgmt(): OnboardingFragment
    }
}
