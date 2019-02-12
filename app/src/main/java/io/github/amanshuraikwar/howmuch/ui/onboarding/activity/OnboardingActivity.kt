package io.github.amanshuraikwar.howmuch.ui.onboarding.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.di.ActivityContext
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.ui.home.HomeActivity
import io.github.amanshuraikwar.howmuch.ui.signin.SignInFragment
import io.github.amanshuraikwar.howmuch.util.Util

class OnboardingActivity
    : BaseActivity<OnboardingContract.View, OnboardingContract.Presenter>()
        , OnboardingContract.View {

    private val tag = Util.getTag(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        initUi()
    }

    private fun initUi() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.contentFl, SignInFragment())
                .commit()
    }

    override fun close() {
        finish()
    }

    override fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    @Module
    abstract class OnboardingModule {

        @Binds
        abstract fun presenter(presenter: OnboardingContract.OnboardingPresenter): OnboardingContract.Presenter

        @Binds
        @ActivityContext
        abstract fun activity(activity: OnboardingActivity): AppCompatActivity
    }
}