package io.github.amanshuraikwar.howmuch.ui.base

import android.annotation.SuppressLint
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Created by amanshuraikwar on 06/03/18.
 */
@SuppressLint("Registered")
abstract class BaseActivity<View: BaseView, Presenter: BasePresenter<View>>
    : DaggerAppCompatActivity(), BaseView {

    @Inject
    protected lateinit var presenter: Presenter

    private var wasViewRecreated: Boolean = true

    @Suppress("UNCHECKED_CAST")
    override fun onResume() {
        super.onResume()

        presenter.attachView(this as View, wasViewRecreated)
        wasViewRecreated = false
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.detachView()
        wasViewRecreated = true
    }
}