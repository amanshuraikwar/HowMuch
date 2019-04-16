package io.github.amanshuraikwar.howmuch.ui.history.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.di.ActivityContext
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.ui.history.HistoryFragment

class HistoryActivity : BaseActivity<HistoryActivityContract.View, HistoryActivityContract.Presenter>()
        , HistoryActivityContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)
    }

    override fun loadFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerFl, HistoryFragment())
                .commit()
    }

    @Module
    abstract class DiModule {

        @Binds
        abstract fun a(a: HistoryActivityContract.PresenterImpl): HistoryActivityContract.Presenter

        @Binds
        @ActivityContext
        abstract fun b(a: HistoryActivity): AppCompatActivity
    }
}