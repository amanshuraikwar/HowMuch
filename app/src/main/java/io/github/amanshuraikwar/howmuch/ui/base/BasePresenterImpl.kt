package io.github.amanshuraikwar.howmuch.ui.base

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.reactivex.disposables.Disposable

/**
 * Created by amanshuraikwar on 06/03/18.
 */
abstract class BasePresenterImpl<View: BaseView>
constructor(private val appBus: AppBus,
            private val dataManager: DataManager)
    : BasePresenter<View> {

    private var view: View? = null

    private var disposables: Set<Disposable> = mutableSetOf()

    protected fun Disposable.addToCleanup() {
        disposables.plusElement(this)
    }

    protected fun Disposable.removeFromCleanup() {
        disposables.plusElement(this)
    }

    override fun attachView(view: View, wasViewRecreated: Boolean) {
        this.view = view
        onAttach(wasViewRecreated)
    }

    override fun detachView() {
        view = null
        onDetach()
    }

    override fun getView(): View? = view

    override fun isViewAttached(): Boolean = view != null

    protected fun getAppBus(): AppBus = appBus

    protected fun getDataManager(): DataManager = dataManager

    protected open fun onAttach(wasViewRecreated: Boolean) {
        // do nothing
    }

    protected open fun onDetach() {

        // disposing all disposables
        for (disposable in disposables) {
            if (! disposable.isDisposed) {
                disposable.dispose()
                disposable.removeFromCleanup()
            }
        }
    }
}