package io.github.amanshuraikwar.howmuch.ui.home

import android.accounts.Account
import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.di.ActivityContext
import io.github.amanshuraikwar.howmuch.ui.addexpense.activity.AddExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.ui.history.HistoryFragment
import io.github.amanshuraikwar.howmuch.ui.onboarding.activity.OnboardingActivity
import io.github.amanshuraikwar.howmuch.ui.signin.SignInFragment
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HomeActivity : BaseActivity<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {

    private val tag = Util.getTag(this)

    //region activity overrides
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }

    private fun init() {
        addTransactionFab.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }
    }

    override fun showLoading(message: String) {
    }

    override fun hideLoading() {
    }

    override fun showError(message: String) {

    }

    override fun showToast(message: String) {
    }

    override fun showSnackbar(message: String) {
    }

    override fun getGoogleAccountCredential(googleAccount: Account): GoogleAccountCredential {
        return GoogleAccountCredential
                .usingOAuth2(this, Arrays.asList(SheetsScopes.SPREADSHEETS))
                .setBackOff(ExponentialBackOff())
                .setSelectedAccount(googleAccount)
    }

    override fun startOnboardingActivity() {
        startActivity(Intent(this, OnboardingActivity::class.java))
    }

    override fun close() {
        finish()
    }

    override fun loadSignInFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerFl, SignInFragment())
                .commit()
    }

    override fun loadHistoryFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerFl, HistoryFragment())
                .commit()
    }

    override fun showAddTransactionBtn() {
        addTransactionFab.visibility = VISIBLE
    }

    @Module
    abstract class HomeModule {

        @Binds
        abstract fun presenter(presenter: HomeContract.HomePresenter): HomeContract.Presenter

        @Binds
        @ActivityContext
        abstract fun activity(activity: HomeActivity): AppCompatActivity
    }
}
