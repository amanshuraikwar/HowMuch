package io.github.amanshuraikwar.howmuch.ui.profile

import android.util.Log
import io.github.amanshuraikwar.howmuch.BuildConfig
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.ui.base.*
import io.github.amanshuraikwar.howmuch.ui.list.Setting
import io.github.amanshuraikwar.howmuch.ui.list.UserInfo
import io.github.amanshuraikwar.howmuch.ui.list.date.DateListItem
import io.github.amanshuraikwar.howmuch.util.Util
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface ProfileContract {

    interface View : BaseView, UiMessageView, LoadingView, GoogleAccountView {
        fun submitList(list: List<ListItem<*, *>>)
        fun initiateSignOut()
        fun showSignOutAlertDialog()
    }

    interface Presenter : BasePresenter<View> {
        fun onSignOutClicked()
    }

    class ProfilePresenter @Inject constructor(appBus: AppBus,
                                               dataManager: DataManager)
        : AccountPresenter<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                refreshUserInfo()
            }
        }

        private fun refreshUserInfo() {

            Observable
                    .just(mutableListOf<ListItem<*,*>>())
                    .flatMap {
                        Observable
                                .just(it)
                                .delay(1000, TimeUnit.MILLISECONDS)
                    }
                    .flatMap {
                        Observable.fromCallable {

                            it.add(
                                    UserInfo.Item(
                                            UserInfo(
                                                    getDisplayName() ?: "HowMuch User",
                                                    getEmail() ?: "user@howmuch.xyz",
                                                    getPhotoUrl() ?: ""
                                            )
                                    )
                            )

                            it.add(
                                    DateListItem(
                                            "Settings"
                                    )
                            )

                            it.add(
                                    Setting.Item(
                                            Setting(
                                                    "Sign Out",
                                                    R.drawable.ic_exit_to_app_white_24dp
                                            )
                                    ).setOnClickListener {
                                        getView()?.showSignOutAlertDialog()
                                    }
                            )

                            it.add(
                                    DateListItem(
                                            BuildConfig.VERSION_NAME
                                    )
                            )

                            it
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

                                Log.e(tag, "refreshUserInfo: Error: ${it.message}")

                                getView()?.run {
                                    showError(it.message ?: "Please try again!")
                                    hideLoading()
                                }
                            }
                    )
                    .addToCleanup()
        }

        override fun onSignOutClicked() {
            getView()?.initiateSignOut()
            getAppBus().onLogout.onNext(Any())
        }
    }
}