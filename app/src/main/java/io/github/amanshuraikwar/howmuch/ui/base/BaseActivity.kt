package io.github.amanshuraikwar.howmuch.ui.base

import android.support.v7.app.AppCompatActivity
import dagger.android.support.DaggerAppCompatActivity
import dagger.internal.DaggerCollections
import javax.inject.Inject

/**
 * Created by amanshuraikwar on 06/03/18.
 */
open class BaseActivity<View: BaseView, Presenter: BasePresenter<View>>
    : DaggerAppCompatActivity(), BaseView {

    @Inject
    lateinit var presenter: Presenter

    private var wasViewRecreated: Boolean = true

//    protected fun getPresenter(): Presenter = presenter

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