package io.github.amanshuraikwar.howmuch.ui.about

import android.util.Log
import io.github.amanshuraikwar.howmuch.BuildConfig
import io.github.amanshuraikwar.howmuch.Constants
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.items.BigCard
import io.github.amanshuraikwar.howmuch.ui.list.items.ProfileBtn
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface AboutContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun submitList(list: List<ListItem<*, *>>)
        fun goToLink(link: String)
    }

    interface Presenter : BasePresenter<View> {
        fun onRetryClicked()
        fun onRefreshClicked()
    }

    class PresenterImpl @Inject constructor(appBus: AppBus,
                                            dataManager: DataManager)
        : HowMuchBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                fetchItems()
            }
        }

        private fun fetchItems() {

            Observable
                    .fromCallable {
                        mutableListOf<ListItem<*, *>>()
                    }
                    .map {
                        prevList ->
                        prevList.add(
                                BigCard.Item(BigCard("HowMuch"))
                        )
                        prevList
                    }
                    .map {
                        prevList ->
                        prevList.add(
                                ProfileBtn.Item(
                                        ProfileBtn(
                                                "Source Code",
                                                R.drawable.github_circle
                                        )
                                ).setOnClickListener {
                                    getView()?.goToLink("https://github.com/amanshuraikwar/HowMuch")
                                }
                        )
                        prevList
                    }
                    .map {
                        prevList ->
                        prevList.add(BigCard.Item(BigCard("Developers")))
                        prevList
                    }
                    .map {
                        prevList ->
                        prevList.add(
                                ProfileBtn.Item(
                                        ProfileBtn(
                                                "Amanshu Raikwar",
                                                R.drawable.baseline_person_24
                                        )
                                ).setOnClickListener {
                                    getView()?.goToLink("https://github.com/amanshuraikwar")
                                }
                        )
                        prevList
                    }
                    .map {
                        prevList ->
                        prevList.add(
                                ProfileBtn.Item(
                                        ProfileBtn(
                                                "Vadluri Vivek",
                                                R.drawable.baseline_person_24
                                        )
                                ).setOnClickListener {
                                    getView()?.goToLink("https://github.com/vadlurivivek")
                                }
                        )
                        prevList
                    }
                    .map {
                        prevList ->
                        prevList.add(BigCard.Item(BigCard(BuildConfig.VERSION_NAME)))
                        prevList
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

                                // todo handle specific error codes

                                getView()?.run {
                                    showError(it.message ?: Constants.DEFAULT_ERROR_MESSAGE)
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