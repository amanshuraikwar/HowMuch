package io.github.amanshuraikwar.howmuch.ui.editcategories

import android.util.Log
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.ui.list.items.EditCategory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface EditCategoriesContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun submitList(list: List<ListItem<*, *>>)
        fun setSyncError()
        fun clearSyncError()
    }

    interface Presenter : BasePresenter<View> {
        fun onRetryClicked()
        fun onRefreshClicked()
    }

    class PresenterImpl @Inject constructor(appBus: AppBus,
                                            dataManager: DataManager)
        : BasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                fetchItems()
            }
        }

        private fun fetchItems() {

            getDataManager()
                    .getAllCategories()
                    .map {
                        it.toList()
                    }
                    .map {

                        val onEditSave: (Category) -> Unit = {
                            category: Category ->
                            updateCategory(category)
                        }

                        val onDelete: (Category) -> Unit = {
                            getView()?.showToast("Deletion of a category is not supported yet.")
                        }

                        it.map {
                            category ->
                            EditCategory.Item(
                                    EditCategory(
                                            category,
                                            onEditSave,
                                            onDelete
                                    )
                            )
                        }
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.run{
                            showLoading("Fetching items...")
                            clearSyncError()
                        }
                    }
                    .subscribe(
                            {

                                Log.d(tag, "fetchItems: Items: $it ")

                                getView()?.run {
                                    submitList(it)
                                    hideLoading()
                                }
                            },
                            {

                                it.printStackTrace()

                                Log.e(tag, "fetchItems: Error: ${it.message}")

                                getView()?.run {
                                    showError(it.message ?: "Please try again!")
                                    hideLoading()
                                    setSyncError()
                                }
                            }
                    )
                    .addToCleanup()
        }

        private fun updateCategory(category: Category) {

            getDataManager()
                    .updateCategory(category)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Saving...")
                    }
                    .subscribe(
                            {
                                getView()?.showToast("Category updated!")
                                fetchItems()
                            },
                            {
                                it.printStackTrace()

                                Log.e(tag, "updateCategory: Error: ${it.message}")

                                getView()?.run {
                                    showError(it.message ?: "Please try again!")
                                    hideLoading()
                                    setSyncError()
                                }
                            }
                    )
                    .addToCleanup()

        }

        override fun onRefreshClicked() {
            fetchItems()
        }

        override fun onRetryClicked() {
            fetchItems()
        }
    }
}