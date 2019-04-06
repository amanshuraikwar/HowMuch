package io.github.amanshuraikwar.howmuch.base.ui.base

/**
 * Base presenter abstraction used throughout the app.
 * Designed to work with app's mvp architecture.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 06/03/18.
 */
interface BasePresenter<View: BaseView> {

    /**
     * Binds presenter with the view when resumed.
     * @param view view associated with this presenter
     */
    fun attachView(view: View, wasViewRecreated: Boolean)

    /**
     * Drops the view when presenter is destroyed.
     */
    fun detachView()

    /**
     * To get view instance
     * @return view instance
     */
    fun getView(): View?

    /**
     * To check if the view is attached
     * @return boolean telling is the view attached to the presenter or not.
     */
    fun isViewAttached(): Boolean
}