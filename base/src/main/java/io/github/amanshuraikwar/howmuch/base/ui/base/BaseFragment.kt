package io.github.amanshuraikwar.howmuch.base.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Base fragment used in the app, designed to work with app's mvp architecture.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 06/03/18.
 */
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

    protected lateinit var activity: AppCompatActivity

    override fun onAttach(context: Context) {
        activity = getActivity() as AppCompatActivity
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        activity = getActivity() as BaseActivity<*, *>
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