package io.github.amanshuraikwar.howmuch.ui.base

/**
 * Created by amanshuraikwar on 06/03/18.
 */
interface BasePresenter<View: BaseView> {

    /**
     * Binds presenter with the view when resumed
     * Presenter should perform initializations here
     * @param view view associated with this presenter
     */
    fun attachView(view: View, wasViewRecreated: Boolean)

    /**
     * Drops the view when presenter is destroyed
     * Presenter should perform clean-ups here
     */
    fun detachView()

    /**
     * to get view instance
     * @return view instance
     */
    fun getView(): View?

    /**
     * to check if the view is attached
     * @return boolean
     */
    fun isViewAttached(): Boolean
}