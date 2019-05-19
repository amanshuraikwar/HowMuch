package io.github.amanshuraikwar.playground.ui.main

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import io.github.amanshuraikwar.howmuch.base.ui.base.BasePresenter
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseView
import io.github.amanshuraikwar.howmuch.base.ui.base.LoadingView
import io.github.amanshuraikwar.howmuch.base.ui.base.UiMessageView
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.playground.data.DataManager
import io.github.amanshuraikwar.playground.data.PlaygroundBus
import io.github.amanshuraikwar.playground.data.Song
import io.github.amanshuraikwar.playground.ui.PlaygroundBasePresenterImpl
import io.github.amanshuraikwar.playground.ui.list.SongItem
import io.github.amanshuraikwar.playground.ui.list.date.HeaderListItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface MainContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun initiateSignIn()
        fun initiateSignOut()
        fun showGoogleUserInfo(name: String, photoUrl: String, email: String)
        fun submitList(list: List<ListItem<*, *>>)
        fun showSettings(name: String, photoUrl: String, email: String)
        fun showActionBtn(msg: String)
        fun hideActionBtn()
    }

    interface Presenter: BasePresenter<View> {
        fun onUserIconClicked()
        fun onSignInResult(isSuccessful: Boolean)
        fun onActionBtnClicked()
        fun onSignOutClicked()
    }

    class PresenterImpl
    @Inject constructor(appBus: PlaygroundBus,
                        dataManager: DataManager)
        : PlaygroundBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private lateinit var onActionBtnClicked: () -> Unit

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                init()
            }
        }

        private fun init() {

            getDataManager()
                    .getSignedInUser()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                user ->
                                getView()?.run {
                                    showGoogleUserInfo(
                                            name = user.name,
                                            email = user.email,
                                            photoUrl = user.userPicUrl ?: ""
                                    )
                                }
                                refreshSongList()
                            },
                            {
                                getView()?.showGoogleUserInfo(
                                        name = "",
                                        email = "",
                                        photoUrl = ""
                                )
                                getView()?.showError("Oops! You are not signed in.")
                                getView()?.showActionBtn("Sign In")
                                onActionBtnClicked = { getView()?.initiateSignIn() }
                            }
                    )
                    .addToCleanup()
        }

        private fun refreshSongList() {

            getDataManager()
                    .getAllSongs()
                    .map { it.getListItems() }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Fetching songs...")
                    }
                    .subscribe(
                            {
                                getView()?.submitList(it)
                                getView()?.hideLoading()
                            },
                            {
                                it.printStackTrace()
                                getView()?.hideLoading()

                                if (it is GoogleJsonResponseException) {

                                    if (it.details.code == 403) {

                                        getView()?.showError("Oops! Looks like you don't have sufficient permissions.")
                                        getView()?.showActionBtn("Sign Out")
                                        onActionBtnClicked = {
                                            onSignOutClicked()
                                            getView()?.initiateSignIn()
                                        }

                                    } else {
                                        getView()?.showError("Oops! Something went wrong.")
                                        getView()?.showActionBtn("Retry")
                                        onActionBtnClicked = { refreshSongList() }
                                    }

                                } else {
                                    getView()?.showError("Oops! Something went wrong.")
                                    getView()?.showActionBtn("Retry")
                                    onActionBtnClicked = { refreshSongList() }
                                }
                            }
                    )
                    .addToCleanup()
        }

        private fun List<Song>.getListItems(): List<ListItem<*, *>> {

            val sorted = this.sortedByDescending { it.timeAdded }

            val list = mutableListOf<ListItem<*, *>>()

            var lastDate = ""
            var curDate: String
            var i = 0
            while (i < sorted.size) {
                curDate = Util.beautifyDate(sorted[i].timeAdded)
                if (lastDate != curDate) {
                    lastDate = Util.beautifyDate(sorted[i].timeAdded)
                    list.add(HeaderListItem(lastDate))
                }
                list.add(SongItem.Item(SongItem(sorted[i])))
                i += 1
            }

            return list
        }

        override fun onUserIconClicked() {

            getDataManager()
                    .getSignedInUser()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                user ->
                                getView()?.showSettings(
                                        name = user.name,
                                        email = user.email,
                                        photoUrl = user.userPicUrl ?: ""
                                )
                            },
                            {
                                // do nothing
                            }
                    )
                    .addToCleanup()
        }

        override fun onSignInResult(isSuccessful: Boolean) {
            if (isSuccessful) {
                init()
            } else {
                getView()?.showError("Oops! Signed in failed.")
                getView()?.showActionBtn("Sign In")
                onActionBtnClicked = { getView()?.initiateSignIn() }
            }
        }

        override fun onActionBtnClicked() {
            onActionBtnClicked.invoke()
        }

        override fun onSignOutClicked() {
            getView()?.submitList(listOf())
            getView()?.initiateSignOut()
            init()
        }
    }
}