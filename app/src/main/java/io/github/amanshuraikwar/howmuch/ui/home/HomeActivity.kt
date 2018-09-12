package io.github.amanshuraikwar.howmuch.ui.home

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.addexpense.AddExpenseFragment
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.ui.history.HistoryFragment
import io.github.amanshuraikwar.howmuch.ui.stats.StatsFragment
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {

    @Suppress("PrivatePropertyName")
    private val TAG = Util.getTag(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (findViewById<View>(android.R.id.content)).systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }

        init()
    }

    override fun loadPage(navigationPage: NavigationPage) {
        when(navigationPage) {
            NavigationPage.ADD_EXPENSE -> loadFragment(AddExpenseFragment())
            NavigationPage.HISTORY -> loadFragment(HistoryFragment())
            NavigationPage.STATS -> loadFragment(StatsFragment())
        }
    }

    private fun init() {
        mainBnv.setOnNavigationItemSelectedListener {
            presenter.onNavigationItemSelected(itemId = it.itemId)
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerFl, fragment)
                .commit()
        return true
    }
}
