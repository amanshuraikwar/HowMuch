package io.github.amanshuraikwar.howmuch.ui.settings

import android.util.Log
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.items.Setting
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SettingsContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun submitList(list: List<ListItem<*, *>>)
        fun initiateSendFeedback()
        fun startDecimalDialog(title: String,
                               initVal: Double,
                               onSuccess: (Double) -> Unit)
    }

    interface Presenter : BasePresenter<View> {
        fun onRetryClicked()
        fun onRefreshClicked()
    }

    class PresenterImpl @Inject constructor(appBus: AppBus,
                                            dataManager: DataManager)
        : HowMuchBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        private val setMonthlyExpenseLimit = {
            limit: Double ->
            getDataManager()
                    .setMonthlyExpenseLimit(limit)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.run{
                            showLoading("Saving expense monthlyLimit...")
                            clearSyncError()
                        }
                    }
                    .subscribe(
                            {
                                fetchItems()
                                getView()?.run {
                                    hideLoading()
                                }
                            },
                            {
                                it.printStackTrace()

                                Log.e(tag, "setMonthlyExpenseLimit: Error: ${it.message}")

                                getView()?.run {
                                    showError(it.message ?: "Please try again!")
                                    hideLoading()
                                    setSyncError()
                                }
                            }
                    )
                    .addToCleanup()
        }

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                fetchItems()
            }
        }

        @Suppress("MoveLambdaOutsideParentheses")
        private fun fetchItems() {

            Observable
                    .fromCallable {
                        mutableListOf<ListItem<*, *>>()
                    }
                    .flatMap {
                        prevList ->
                        getDataManager()
                                .getMonthlyExpenseLimit()
                                .map {
                                    limit ->
                                    prevList.add(
                                            Setting.Item(
                                                    Setting(
                                                            "Monthly expense monthlyLimit",
                                                            "Maximum amount that you prefer spending in a month",
                                                            R.drawable.ic_local_atm_white_24dp
                                                    )
                                            ).setOnClickListener {
                                                getView()?.startDecimalDialog(
                                                        "Monthly expense monthlyLimit",
                                                        limit,
                                                        setMonthlyExpenseLimit
                                                )
                                            }
                                    )
                                    prevList
                                }
                    }
                    .map {
                        prevList ->
                        prevList.add(
                                Setting.Item(
                                        Setting(
                                                "Send feedback",
                                                "Report technical issues or suggest new features",
                                                R.drawable.ic_feedback_white_24dp
                                        )
                                ).setOnClickListener {
                                    getView()?.initiateSendFeedback()
                                }
                        )
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