package io.github.amanshuraikwar.howmuch.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.ui.addexpense.AddExpenseFragment
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.ui.history.HistoryFragment
import io.github.amanshuraikwar.howmuch.ui.stats.StatsFragment
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject
import kotlin.math.log

class HomeActivity : BaseActivity<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {

    @Suppress("PrivatePropertyName")
    private val TAG = Util.getTag(this)

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var historyFragment: HistoryFragment

    //region activity overrides
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (findViewById<View>(android.R.id.content)).systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }

        init()
    }

    private fun init() {
        mainBnv.setOnNavigationItemSelectedListener {
            presenter.onNavigationItemSelected(itemId = it.itemId)
            return@setOnNavigationItemSelectedListener true
        }

        signInBtn.setOnClickListener {
            presenter.onSignInBtnClicked()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AuthenticationManager.CODE_SIGN_IN) {
            presenter.onSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data).isSuccessful)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        Log.d(TAG, "handleSignInResult:called")
        Log.i(TAG, "handleSignInResult:task is successful = " + task.isSuccessful)

        try {

        } catch (exception: ApiException) {

        }
    }

    //endregion

    //region view overrides
    override fun loadPage(navigationPage: NavigationPage) {
        when(navigationPage) {
            NavigationPage.ADD_EXPENSE -> loadFragment(AddExpenseFragment())
            NavigationPage.HISTORY -> loadFragment(historyFragment)
            NavigationPage.STATS -> loadFragment(StatsFragment())
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerFl, fragment)
                .commit()
        return true
    }

    override fun hideBnv() {
        mainBnv.visibility = View.GONE
    }

    override fun showBnv() {
        mainBnv.visibility = View.VISIBLE
    }

    override fun showSignInScreen() {
        signInFl.visibility = View.VISIBLE
    }

    override fun hideSignInScreen() {
        signInFl.visibility = View.GONE
    }

    override fun hideMainFragmentContainer() {
        fragmentContainerFl.visibility = View.GONE
    }

    override fun showMainFragmentContainer() {
        fragmentContainerFl.visibility = View.VISIBLE
    }

    override fun initiateSignIn() {
        startActivityForResult(googleSignInClient.signInIntent, AuthenticationManager.CODE_SIGN_IN)
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    //endregion
}
