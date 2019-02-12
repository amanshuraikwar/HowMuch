package io.github.amanshuraikwar.howmuch.ui.settings

import io.github.amanshuraikwar.howmuch.bus.AppBus
import io.github.amanshuraikwar.howmuch.data.DataManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.ui.base.AccountPresenter
import io.github.amanshuraikwar.howmuch.ui.base.BasePresenterImpl
import io.github.amanshuraikwar.howmuch.util.ExtendedCurrency
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SettingsPresenter
    @Inject constructor(appBus: AppBus,
                        dataManager: DataManager)
    : AccountPresenter<SettingsContract.View>(appBus, dataManager), SettingsContract.Presenter {

    @Suppress("PrivatePropertyName")
    private val TAG = Util.getTag(this)

    override fun onAttach(wasViewRecreated: Boolean) {
        super.onAttach(wasViewRecreated)

        if (wasViewRecreated) {
            init()
        }
    }

    private fun init() {
//        getView()?.run {
//            updateProfilePic(getGoogleSignInAccount()?.photoUrl.toString())
//            updateName(getGoogleSignInAccount()?.displayName ?: "")
//            updateEmail(getEmail())
//            updateCurrency(getDataManager().getCurrency())
//        }
//
//        getAppBus()
//                .onCurrencyChanged
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    getView()?.updateCurrency(getDataManager().getCurrency())
//                }
    }

    override fun onCurrencyClicked() {
        getView()?.showCurrencyDialog(ExtendedCurrency.getAll().map { it.symbol })
    }

    override fun onAboutClicked() {
        //
    }

    override fun onLogoutClicked() {
        getView()?.initiateSignOut()
        getAppBus().onLogout.onNext(Any())
    }

    override fun onCurrencySelected(which: String) {
        getDataManager().setCurrency(which)
        getAppBus().onCurrencyChanged.onNext(which)
    }
}