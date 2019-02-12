package io.github.amanshuraikwar.howmuch.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Base fragment used in the app, designed to work with app's mvp architecture.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 06/03/18.
 */
@SuppressLint("Registered")
abstract class BaseFragment<View: BaseView, Presenter: BasePresenter<View>>
    : DaggerFragment(), BaseView {

    /**
     * Presenter instance for the current view.
     */
    @Inject
    protected lateinit var presenter: Presenter

    /**
     * Boolean to tell whether the current fragment was recreated.
     * Generally queried by the presenter.
     */
    private var wasViewRecreated: Boolean = true

    protected lateinit var activity: BaseActivity<*, *>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as io.github.amanshuraikwar.howmuch.ui.base.BaseActivity<*, *>
    }

    @Suppress("UNCHECKED_CAST")
    override fun onResume() {
        super.onResume()

        presenter.attachView(this as View, wasViewRecreated)
        wasViewRecreated = false
    }

    override fun onDestroyView() {
        super.onDestroyView()

        presenter.detachView()
        wasViewRecreated = true
    }
}