package io.github.amanshuraikwar.howmuch.ui.demo

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.ui.createsheet.CreateSheetFragment
import io.github.amanshuraikwar.howmuch.ui.signin.SignInFragment
import javax.inject.Inject

class DemoActivity : BaseActivity<DemoContract.View, DemoContract.Presenter>(), DemoContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (findViewById<View>(android.R.id.content)).systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            window.navigationBarColor = getColor(R.color.gray1)
        }

    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment): Boolean {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.parentFl, fragment)
                .commit()
        return true
    }
}