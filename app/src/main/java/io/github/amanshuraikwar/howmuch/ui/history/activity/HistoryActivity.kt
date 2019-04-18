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

    companion object {
        // Filters for the transaction list to be shown
        // Format: transaction_type=DEBIT|CREDIT|ALL&category_id=id1|id2|id3&wallet_id=id1|id2|id3
        const val KEY_FILTERS = "filters"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)
    }

    override fun loadFragment() {

        supportFragmentManager.beginTransaction()
                .replace(
                        R.id.containerFl,
                        {
                            val fragment = HistoryFragment()
                            val bundle = Bundle()
                            bundle.putString(
                                    HistoryFragment.KEY_FILTERS,
                                    intent.getStringExtra(KEY_FILTERS)
                            )
                            fragment.arguments = bundle
                            fragment
                        }.invoke()
                )
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