package io.github.amanshuraikwar.howmuch.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.di.ActivityContext
import io.github.amanshuraikwar.howmuch.ui.addexpense.AddExpenseActivity
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.ui.categories.CategoriesFragment
import io.github.amanshuraikwar.howmuch.ui.profile.ProfileFragment
import io.github.amanshuraikwar.howmuch.ui.signin.SignInFragment
import io.github.amanshuraikwar.howmuch.ui.stats.StatsFragment
import io.github.amanshuraikwar.howmuch.ui.wallets.WalletsFragment
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
                    showAddTransactionBtn()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_categories -> {
                    loadFragment(CategoriesFragment())
                    showAddTransactionBtn()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_wallets -> {
                    loadFragment(WalletsFragment())
                    showAddTransactionBtn()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_profile-> {
                    loadFragment(ProfileFragment())
                    hideAddTransactionBtn()
                    return@setOnNavigationItemSelectedListener true
                }

                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
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
        addTransactionFab.show()
    }

    override fun showBnv() {
        bnv.visibility = VISIBLE
    }

    override fun hideAddTransactionBtn() {
        addTransactionFab.hide()
    }

    override fun hideBnv() {
        bnv.visibility = GONE
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
