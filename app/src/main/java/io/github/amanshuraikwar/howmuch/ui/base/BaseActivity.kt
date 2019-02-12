package io.github.amanshuraikwar.howmuch.ui.base

import android.annotation.SuppressLint
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Base activity used in the app, designed to work with app's mvp architecture.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 06/03/18.
 */
@SuppressLint("Registered")
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

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

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