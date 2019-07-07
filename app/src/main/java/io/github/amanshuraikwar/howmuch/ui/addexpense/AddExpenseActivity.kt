package io.github.amanshuraikwar.howmuch.ui.addexpense

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.di.ActivityContext
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_add_expense.*

class AddExpenseActivity
    : BaseActivity<AddExpenseActivityContract.View, AddExpenseActivityContract.Presenter>(), AddExpenseActivityContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)
        toolbar.setNavigationIcon(R.drawable.round_close_24)
        toolbar.setNavigationOnClickListener {
            this.finish()
        }
    }

    override fun loadFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.contentFl, AddExpenseFragment())
                .commitAllowingStateLoss()
    }

    override fun close(success: Boolean) {
        setResult(if (success) Activity.RESULT_OK else Activity.RESULT_CANCELED)
        finish()
    }

    @Module
    abstract class DiModule {

        @Binds
        abstract fun a(a: AddExpenseActivityContract.PresenterImpl): AddExpenseActivityContract.Presenter

        @Binds
        @ActivityContext
        abstract fun b(a: AddExpenseActivity): AppCompatActivity
    }
}