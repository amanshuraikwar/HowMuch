package io.github.amanshuraikwar.howmuch.ui.profile

import android.util.Log
import io.github.amanshuraikwar.howmuch.Constants
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.protocol.User
import io.github.amanshuraikwar.howmuch.ui.list.items.ProfileBtn
import io.github.amanshuraikwar.howmuch.ui.list.items.UserInfo
import io.github.amanshuraikwar.howmuch.base.util.Util;
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.github.amanshuraikwar.howmuch.ui.list.items.Divider
import io.github.amanshuraikwar.howmuch.ui.list.items.DividerFrontPadded
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface ProfileContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun submitList(list: List<ListItem<*, *>>)
        fun initiateSignOut()
        fun showSignOutAlertDialog()
        fun startHistoryActivity()
        fun startAboutActivity()
        fun openGoogleSpreadSheet(spreadsheetId: String)
    }

    interface Presenter : BasePresenter<View> {
        fun onSignOutClicked()
    }

    class ProfilePresenter @Inject constructor(appBus: AppBus,
                                               dataManager: DataManager)
        : HowMuchBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                refreshUserInfo()
            }
        }

        private fun refreshUserInfo() {

            Observable
                    .fromCallable {
                        mutableListOf<ListItem<*,*>>()
                    }
                    .flatMap {
                        list ->
                        getDataManager()
                                .getSignedInUser()
                                .delay(1000, TimeUnit.MILLISECONDS)
                                .map {
                                    list.add(getUserItem(it))
                                    list.add(Divider.Item())
                                    list
                                }
                                .flatMap {
                                    getDataManager()
                                            .getSpreadSheetId()
                                            .map {
                                                list.add(getSpreadSheetItem(it))
                                                list.add(Divider.Item())
                                                list
                                            }
                                }
                                .map {
                                    list.addAll(getItems())
                                    list
                                }
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Fetching data...")
                    }
                    .subscribe(
                            {

                                getView()?.run {
                                    submitList(it)
                                    hideLoading()
                                }
                            },
                            {

                                it.printStackTrace()
                                Log.e(tag, "refreshUserInfo: getSignedInUser", it)

                                // todo handle specific error codes

                                getView()?.run {
                                    showError(it.message ?: Constants.DEFAULT_ERROR_MESSAGE)
                                    hideLoading()
                                }
                            }
                    )
                    .addToCleanup()
        }

        private fun getSpreadSheetItem(it: String): ListItem<*, *> {
            return ProfileBtn.Item(
                    ProfileBtn(
                            "Google Spreadsheet",
                            R.drawable.round_table_chart_24
                    )
            ).setOnClickListener {
                getView()?.openGoogleSpreadSheet(it)
            }
        }

        private fun getUserItem(user: User): ListItem<*, *> {

            return UserInfo.Item(
                    UserInfo(
                            user.name,
                            user.email,
                            user.userPicUrl ?: ""
                    )
            )
        }

        private fun getItems(): List<ListItem<*, *>> {

            val list = mutableListOf<ListItem<*, *>>()

            list.add(
                    ProfileBtn.Item(
                            ProfileBtn(
                                    "History",
                                    R.drawable.round_history_24
                            )
                    ).setOnClickListener {
                        getView()?.startHistoryActivity()
                    }
            )

            list.add(Divider.Item())

            list.add(
                    ProfileBtn.Item(
                            ProfileBtn(
                                    "About",
                                    R.drawable.round_new_releases_24
                            )
                    ).setOnClickListener {
                        getView()?.startAboutActivity()
                    }
            )

            list.add(Divider.Item())

            list.add(
                    ProfileBtn.Item(
                            ProfileBtn(
                                    "Sign Out",
                                    R.drawable.round_exit_to_app_24
                            )
                    ).setOnClickListener {
                        getView()?.showSignOutAlertDialog()
                    }
            )

            return list
        }

        override fun onSignOutClicked() {
            getView()?.initiateSignOut()
            getAppBus().onSignOut.onNext(Any())
        }
    }
}