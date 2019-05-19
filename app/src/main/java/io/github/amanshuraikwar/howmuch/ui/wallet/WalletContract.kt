package io.github.amanshuraikwar.howmuch.ui.wallet

import android.util.Log
import io.github.amanshuraikwar.howmuch.Constants
import io.github.amanshuraikwar.howmuch.base.bus.AppBus
import io.github.amanshuraikwar.howmuch.base.data.DataManager
import io.github.amanshuraikwar.howmuch.base.ui.base.*
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.ui.HowMuchBasePresenterImpl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import javax.inject.Inject

interface WalletContract {

    interface View : BaseView, UiMessageView, LoadingView {
        fun getWallet(): Wallet
        fun setWallet(wallet: Wallet)
        fun showWallet(name: String,
                       balance: String)
        fun showEditMode()
        fun hideEditMode()
        fun close(success: Boolean)
        fun showEditCloseDialog()
        fun showDeleteDialog()
        fun showNameError(msg: String)
        fun showBalanceError(msg: String)
    }

    interface Presenter : BasePresenter<View> {
        fun onEditCloseClicked()
        fun onBackBtnClicked()
        fun onEditBtnClicked()
        fun onEditSaveClicked(name: String,
                              balance: String)
        fun onEditDiscardClicked()
        fun onDeleteBtnClicked()
        fun onDeleteConfirmedClicked()
    }

    class WalletNotFoundException(msg: String) : Exception(msg)

    class PresenterImpl @Inject constructor(appBus: AppBus,
                                            dataManager: DataManager)
        : HowMuchBasePresenterImpl<View>(appBus, dataManager), Presenter {

        private val tag = Util.getTag(this)

        private lateinit var curWallet: Wallet

        private var updated: Boolean = false

        override fun onAttach(wasViewRecreated: Boolean) {
            super.onAttach(wasViewRecreated)
            if (wasViewRecreated) {
                init()
            }
        }

        private fun init() {

            Observable
                    .fromCallable {

                        getView()?.getWallet()
                                ?: throw WalletNotFoundException(
                                        "Wallet was not returned from the view!"
                                )
                    }
                    .subscribe (
                            {
                                curWallet = it
                                curWallet.show()
                                getView()?.hideEditMode()
                            },
                            {

                            }
                    )
                    .addToCleanup()
        }

        private fun Wallet.show() {
            getView()?.showWallet(this.name, this.balance.toString())
        }

        override fun onEditCloseClicked() {
            getView()?.showEditCloseDialog()
        }

        override fun onBackBtnClicked() {
            getView()?.close(updated)
        }

        override fun onEditBtnClicked() {
            getView()?.showEditMode()
        }

        override fun onEditSaveClicked(name: String,
                                       balance: String) {

            if (name.isEmpty()) {
                getView()?.showNameError("Name cannot be empty!")
            }

            if (balance.isEmpty()) {
                getView()?.showBalanceError("Balance cannot be empty!")
            }

            val newWallet = Wallet(curWallet.id, name, balance.toDouble(), curWallet.active)

            getDataManager()
                    .updateWallet(newWallet)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Saving...")
                    }
                    .subscribe(
                            {
                                updated = true
                                getView()?.setWallet(newWallet)
                                init()
                                getView()?.showSnackbar("Wallet updated!")
                                getView()?.hideLoading()
                            },
                            {
                                it.printStackTrace()
                                Log.e(tag, "onEditSaveClicked: updateWallet", it)

                                // todo handle specific errors

                                getView()?.run {
                                    hideLoading()
                                    showError(it.message ?: Constants.DEFAULT_ERROR_MESSAGE)
                                }
                            }
                    )
                    .addToCleanup()
        }

        override fun onEditDiscardClicked() {
            curWallet.show()
            getView()?.hideEditMode()
        }

        override fun onDeleteBtnClicked() {
            getView()?.showDeleteDialog()
        }

        override fun onDeleteConfirmedClicked() {
            getDataManager()
                    .deleteWallet(curWallet)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        getView()?.showLoading("Deleting...")
                    }
                    .subscribe(
                            {
                                updated = true
                                getView()?.run {
                                    close(updated)
                                    hideLoading()
                                }
                            },
                            {
                                it.printStackTrace()
                                Log.e(tag, "onDeleteConfirmedClicked: deleteWallet", it)

                                // todo handle specific error codes

                                getView()?.run {
                                    hideLoading()
                                    showError(it.message ?: Constants.DEFAULT_ERROR_MESSAGE)
                                }
                            }
                    )
                    .addToCleanup()
        }
    }
}