package io.github.amanshuraikwar.howmuch.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.di.ActivityContext
import io.github.amanshuraikwar.howmuch.ui.addexpense.AddExpenseActivity
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.ui.history.HistoryFragment
import io.github.amanshuraikwar.howmuch.ui.signin.SignInFragment
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
