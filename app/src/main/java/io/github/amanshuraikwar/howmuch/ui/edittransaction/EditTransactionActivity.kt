package io.github.amanshuraikwar.howmuch.ui.edittransaction

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.data.local.room.transaction.Transaction
import io.github.amanshuraikwar.howmuch.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.util.LogUtil
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.android.synthetic.main.activity_edit_transaction.*

/**
 * Created by amanshuraikwar on 09/03/18.
 */
class EditTransactionActivity
    : BaseActivity<EditTransactionContract.View, EditTransactionContract.Presenter>(),
        EditTransactionContract.View {

    private val TAG = LogUtil.getLogTag(this)

    companion object {
        const val KEY_TRANSACTION = "transaction"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)

        initUi()
    }

    private fun initUi() {

        editTransactionDateTv.text =
                Util.formatDate(getCurTransaction().getDateAdded()) +
                " " + Util.formatTime(getCurTransaction().getDateAdded())

        editTransactionAmountTv.setText(getCurTransaction().getAmount().toString())

        editTransactionDoneBtn.setOnClickListener {
            presenter.onDoneBtnClick(
                    editTransactionAmountTv.text.toString(),
                    editTransactionTypeTv.text.toString())
        }

        editTransactionBackIb.setOnClickListener {
            closeActivity()
        }

        /*
        transactionTypeTv.setOnClickListener({
            if (transactionTypeTv.text == "-") {
                transactionTypeTv.text = "+"
                transactionTypeTv.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGreen))
            } else {
                transactionTypeTv.text = "-"
                transactionTypeTv.setBackgroundColor(ContextCompat.getColor(this, R.color.lightRed))
            }
        })
        */
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    override fun closeActivity() {
        finishAfterTransition()
    }

    override fun getCurTransaction()
            : Transaction =
            intent.getParcelableExtra(KEY_TRANSACTION)
}