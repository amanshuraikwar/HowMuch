package io.github.amanshuraikwar.howmuch.ui.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import butterknife.BindArray
import butterknife.ButterKnife
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.addexpense.AddExpenseFragment
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.ui.history.HistoryFragment
import io.github.amanshuraikwar.howmuch.ui.signin.SignInFragment
import io.github.amanshuraikwar.howmuch.ui.stats.StatsFragment
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : BaseActivity<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {

    @Suppress("PrivatePropertyName")
    private val TAG = Util.getTag(this)

    @Inject
    lateinit var historyFragment: HistoryFragment

    @Inject
    lateinit var addExpenseFragment: AddExpenseFragment

    @Inject
    lateinit var signInFragment: SignInFragment

    @Inject
    lateinit var statsFragment: StatsFragment

    @BindArray(R.array.tab_names)
    lateinit var tabs: Array<String>

    private val tabIconsDrawable =
            intArrayOf(
                    R.drawable.home_drawable_selector,
                    R.drawable.history_drawable_selector,
                    R.drawable.stats_drawable_selector)

    //region activity overrides
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ButterKnife.bind(this)

        // todo ponder on this while final designing
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (findViewById<View>(android.R.id.content)).systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        */

        init()
        initTabs()
    }

    private fun init() {

    }

    private fun initTabs() {

        if (bottomTl != null) {
            for (i in tabs.indices) {
                bottomTl.addTab(bottomTl.newTab())
                val tab = bottomTl.getTabAt(i)
                tab?.customView = getTabView(i)
            }
        }

        bottomTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                presenter.onNavigationItemSelected(p0?.position ?: 0)
                updateTabSelection(p0?.position ?: 0)
            }

        })

        updateTabSelection(0)
    }

    private fun getTabView(position: Int): View {

        val view = LayoutInflater.from(this).inflate(R.layout.item_bottom_tab, null)
        val icon = view.findViewById<ImageView>(R.id.tabIcon)
        icon.setImageDrawable(ContextCompat.getDrawable(this, tabIconsDrawable[position]))
        return view
    }

    private fun updateTabSelection(currentTab: Int) {

        for (i in tabs.indices) {
            val selectedTab = bottomTl.getTabAt(i)
            selectedTab!!.customView!!.isSelected = currentTab == i
        }
    }
    //endregion

    //region view overrides
    override fun loadPage(navigationPage: NavigationPage) {
        when(navigationPage) {
            NavigationPage.ADD_EXPENSE -> loadFragment(addExpenseFragment)
            NavigationPage.HISTORY -> loadFragment(historyFragment)
            NavigationPage.STATS -> loadFragment(statsFragment)
            NavigationPage.SIGN_IN -> loadFragment(signInFragment)
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerFl, fragment)
                .commit()
        return true
    }

    override fun hideBottomNav() {
        bottomTl.visibility = View.GONE
    }

    override fun showBottomNav() {
        bottomTl.visibility = View.VISIBLE
    }

    override fun hideMainFragmentContainer() {
        fragmentContainerFl.visibility = View.GONE
    }

    override fun showMainFragmentContainer() {
        fragmentContainerFl.visibility = View.VISIBLE
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    //endregion
}
