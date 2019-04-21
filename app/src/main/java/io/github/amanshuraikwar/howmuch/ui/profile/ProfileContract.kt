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
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
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
        fun startWalletsActivity()
        fun startCategoriesActivity()
        fun startSettingsActivity()
    }

    interface Presenter : BasePresenter<View> {
        fun onSignOutClicked()
    }

    class ProfilePresenter @Inject constructor(appBus: AppBus,
                                               dataManager: DataManager)
        : BasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                refreshUserInfo()
            }
        }

        private fun refreshUserInfo() {

            getDataManager()
                    .getSignedInUser()
                    .delay(1000, TimeUnit.MILLISECONDS)
                    .map { toInitList(it) }
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

        private fun toInitList(user: User): List<ListItem<*, *>> {

            val list = mutableListOf<ListItem<*, *>>()

            list.add(
                    UserInfo.Item(
                            UserInfo(
                                    user.name,
                                    user.email,
                                    user.userPicUrl ?: ""
                            )
                    )
            )

            list.add(
                    ProfileBtn.Item(
                            ProfileBtn(
                                    "Wallets",
                                    R.drawable.ic_credit_card_white_24dp
                            )
                    ).setOnClickListener {
                        getView()?.startWalletsActivity()
                    }
            )

            list.add(
                    ProfileBtn.Item(
                            ProfileBtn(
                                    "Categories",
                                    R.drawable.ic_bubble_chart_white_24dp
                            )
                    ).setOnClickListener {
                        getView()?.startCategoriesActivity()
                    }
            )

            list.add(
                    ProfileBtn.Item(
                            ProfileBtn(
                                    "History",
                                    R.drawable.ic_history_white_24dp
                            )
                    ).setOnClickListener {
                        getView()?.startHistoryActivity()
                    }
            )

            list.add(
                    ProfileBtn.Item(
                            ProfileBtn(
                                    "Settings",
                                    R.drawable.ic_settings_white_24dp
                            )
                    ).setOnClickListener {
                        getView()?.startSettingsActivity()
                    }
            )

            list.add(
                    ProfileBtn.Item(
                            ProfileBtn(
                                    "About",
                                    R.drawable.ic_new_releases_white_24dp
                            )
                    ).setOnClickListener {
                        getView()?.showToast("About clicked.")
                    }
            )

            list.add(
                    ProfileBtn.Item(
                            ProfileBtn(
                                    "Sign Out",
                                    R.drawable.ic_exit_to_app_white_24dp
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