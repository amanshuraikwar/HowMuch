package io.github.amanshuraikwar.howmuch.ui.base

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * BasePresenter's implementation.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 06/03/18.
 */
abstract class BasePresenterImpl<View: BaseView>
constructor(private val appBus: AppBus,
            private val dataManager: DataManager)
    : BasePresenter<View> {

    private var view: View? = null

    //region RxJava Disposable methods
    private var disposables: CompositeDisposable = CompositeDisposable()

    /**
     * Add the current disposable to be disposed/cleaned when the view is detached.
     * Using Kotlin's inbuilt decorator design pattern :)
     */
    protected fun Disposable.addToCleanup() {
        disposables.add(this)
    }

    /**
     * Remove the current disposable from being disposed/cleaned when the view is detached.
     * Using Kotlin's inbuilt decorator design pattern :)
     */
    protected fun Disposable.removeFromCleanup() {
        disposables.remove(this)
    }
    //endregion

    //region BasePresenter implemented methods
    final override fun attachView(view: View, wasViewRecreated: Boolean) {
        this.view = view
        onAttach(wasViewRecreated)
    }

    final override fun detachView() {
        view = null
        onDetach()
    }

    final override fun getView(): View? = view

    final override fun isViewAttached(): Boolean = view != null
    //endregion

    /**
     * To get the AppBus instance.
     *
     * @return AppBus instance.
     */
    protected fun getAppBus(): AppBus = appBus

    /**
     * To get DataManager instance.
     *
     * @return DataManager instance.
     */
    protected fun getDataManager(): DataManager = dataManager

    /**
     * Called when the view is attached to the presenter.
     * Presenter should perform initializations here.
     *
     * @param wasViewRecreated boolean telling if the corresponding view was recreated.
     */
    protected open fun onAttach(wasViewRecreated: Boolean) {
        // do nothing
    }

    /**
     * Called when the view is detached from the presenter.
     * Presenter should perform clean-ups here.
     */
    protected open fun onDetach() {

        // disposing all disposables
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }
}