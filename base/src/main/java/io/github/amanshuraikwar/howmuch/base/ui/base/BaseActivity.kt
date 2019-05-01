package io.github.amanshuraikwar.howmuch.base.ui.base

import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Base activity used in the app, designed to work with app's mvp architecture.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 06/03/18.
 */
abstract class BaseActivity<View: BaseView, Presenter: BasePresenter<View>>
    : DaggerAppCompatActivity(), BaseView {

    /**
     * Presenter instance for the current view.
     */
    @Inject
    protected lateinit var presenter: Presenter

    /**
     * Boolean to tell whether the current activity was recreated.
     * Generally queried by the presenter.
     */
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