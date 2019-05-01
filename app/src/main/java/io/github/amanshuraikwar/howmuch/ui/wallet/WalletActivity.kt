package io.github.amanshuraikwar.howmuch.ui.wallet

import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.di.ActivityContext
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.protocol.Wallet
import kotlinx.android.synthetic.main.activity_wallet.*
import kotlinx.android.synthetic.main.layout_loading_overlay.*

class WalletActivity
    : BaseActivity<WalletContract.View, WalletContract.Presenter>(), WalletContract.View {

    companion object {
        const val KEY_WALLET = "wallet"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
    }

    override fun onBackPressed() {
        presenter.onBackBtnClicked()
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showSnackbar(message: String) {
    }

    override fun showLoading(message: String) {
        loadingParentLl.visibility = VISIBLE
        loadingTv.text = message
    }

    override fun hideLoading() {
        loadingParentLl.visibility = GONE
        loadingTv.text = ""
    }

    override fun getWallet(): Wallet {
        return intent.getParcelableExtra(KEY_WALLET)
    }

    override fun setWallet(wallet: Wallet) {
        intent.putExtra(KEY_WALLET, wallet)
    }

    override fun showWallet(name: String, balance: String) {
        nameEt.setText(name)
        balanceEt.setText(balance)
    }

    override fun showEditMode() {

        backIb.setImageResource(R.drawable.ic_close_white_24dp)
        backIb.setOnClickListener {
            presenter.onEditCloseClicked()
        }

        editIb.visibility = GONE
        editIb.setOnClickListener(null)

        deleteIb.visibility = GONE
        deleteIb.setOnClickListener(null)

        doneBtn.visibility = VISIBLE
        doneBtn.setOnClickListener {
            presenter.onEditSaveClicked(
                    nameEt.text.toString(),
                    balanceEt.text.toString()
            )
        }

        nameEt.inputType = InputType.TYPE_CLASS_TEXT
        balanceEt.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

    }

    override fun hideEditMode() {

        backIb.setImageResource(R.drawable.ic_arrow_back_white_24dp)
        backIb.setOnClickListener {
            presenter.onBackBtnClicked()
        }

        editIb.visibility = VISIBLE
        editIb.setOnClickListener {
            presenter.onEditBtnClicked()
        }

        deleteIb.visibility = VISIBLE
        deleteIb.setOnClickListener{
            presenter.onDeleteBtnClicked()
        }

        doneBtn.visibility = GONE
        doneBtn.setOnClickListener(null)

        nameEt.inputType = InputType.TYPE_NULL
        nameEt.clearFocus()
        balanceEt.inputType = InputType.TYPE_NULL
        balanceEt.clearFocus()
    }

    override fun close(success: Boolean) {
        setResult(if(success) Activity.RESULT_OK else Activity.RESULT_CANCELED)
        finish()
    }

    override fun showEditCloseDialog() {
        AlertDialog
                .Builder(this)
                .setMessage(R.string.edit_close_message)
                .setNegativeButton("Discard") {
                    dialog, _ ->
                    presenter.onEditDiscardClicked()
                    dialog.dismiss()
                }
                .setPositiveButton("Keep editing") { dialog, _ -> dialog.dismiss() }
                .setCancelable(true)
                .show()
    }

    override fun showDeleteDialog() {
        AlertDialog
                .Builder(this)
                .setMessage(R.string.delete_confirm)
                .setNegativeButton("Delete") {
                    dialog, _ ->
                    presenter.onDeleteConfirmedClicked()
                    dialog.dismiss()
                }
                .setPositiveButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .setCancelable(true)
                .show()
    }

    override fun showNameError(msg: String) {
        nameEt.error = msg
    }

    override fun showBalanceError(msg: String) {
        balanceEt.error = msg
    }

    @Module
    abstract class WalletModule {

        @Binds
        abstract fun a(a: WalletContract.PresenterImpl): WalletContract.Presenter

        @Binds
        @ActivityContext
        abstract fun b(activity: WalletActivity): AppCompatActivity
    }
}