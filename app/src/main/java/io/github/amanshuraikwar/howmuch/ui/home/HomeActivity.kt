package io.github.amanshuraikwar.howmuch.ui.home

import android.os.Bundle
import android.support.design.widget.Snackbar
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by amanshuraikwar on 07/03/18.
 */
class HomeActivity
    : BaseActivity<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
}