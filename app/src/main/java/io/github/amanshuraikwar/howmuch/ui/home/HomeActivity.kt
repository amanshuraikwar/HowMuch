package io.github.amanshuraikwar.howmuch.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.BuildConfig
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.di.ActivityContext
import io.github.amanshuraikwar.howmuch.ui.addexpense.AddExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.ui.history.HistoryFragment
import io.github.amanshuraikwar.howmuch.ui.signin.SignInFragment
import io.github.amanshuraikwar.howmuch.ui.stats.StatsFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {

    companion object {
        private const val REQ_CODE_TRANSACTION = 10069
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_TRANSACTION) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.onTransactionAdded()
            }
        }
    }

    private fun init() {

        addTransactionFab.setOnClickListener {
            startActivityForResult(
                    Intent(this, AddExpenseActivity::class.java),
                    REQ_CODE_TRANSACTION
            )
        }

        bnv.setOnNavigationItemSelectedListener {

            when(it.itemId) {

                R.id.navigation_stats -> {
                    loadFragment(StatsFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_history -> {
                    loadFragment(HistoryFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }

        versionTv.text =  "[${BuildConfig.FLAVOR}] ${BuildConfig.VERSION_NAME} [${BuildConfig.FLAVOR}]"
    }

    override fun close() {
        finish()
    }

    override fun loadSignInFragment() {
        loadFragment(SignInFragment())
    }

    override fun loadHistoryFragment() {
        loadFragment(StatsFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerFl, fragment)
                .commit()
    }

    override fun showAddTransactionBtn() {
        addTransactionFab.visibility = VISIBLE
    }

    override fun showBnv() {
        bnv.visibility = VISIBLE
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
