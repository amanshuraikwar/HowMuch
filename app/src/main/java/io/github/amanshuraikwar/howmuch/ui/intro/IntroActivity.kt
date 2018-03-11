package io.github.amanshuraikwar.howmuch.ui.intro

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.ui.home.HomeActivity
import io.github.amanshuraikwar.howmuch.util.LogUtil
import kotlinx.android.synthetic.main.activity_intro.*

/**
 * Created by amanshuraikwar on 09/03/18.
 */
class IntroActivity
    : BaseActivity<IntroContract.View, IntroContract.Presenter>(), IntroContract.View {

    private val TAG = LogUtil.getLogTag(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        initUi()
    }

    private fun initUi() {

        doneBtn.setOnClickListener({
            presenter.onDoneClick(dailyAmountTv.text.toString())
        })
    }

    override fun showError(error: String) {
        Snackbar.make(parentCl, error, Snackbar.LENGTH_LONG).show()
    }

    override fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}