package io.github.amanshuraikwar.howmuch.ui.expense

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ViewUtil
import io.github.amanshuraikwar.howmuch.base.util.Util
import io.github.amanshuraikwar.howmuch.protocol.Category
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import kotlinx.android.synthetic.main.dialog_transaction.view.*

class TransactionDialog(val activity: FragmentActivity) {

    private val view: View = activity.layoutInflater.inflate(R.layout.dialog_transaction, null)
    private val dialog: AlertDialog

    init {
        dialog =
                MaterialAlertDialogBuilder(activity)
                        .setView(view)
                        .create()
    }

    fun start(transaction: Transaction, category: Category) {

        view.amountTv.text = transaction.amount.toString()
        view.titleTv.text = transaction.title
        view.categoryIv.setImageResource(ViewUtil.getCategoryIcon(category.name))
        view.categoryTv.text = category.name
        view.dateTv.text = Util.beautifyDate(transaction.date)
        view.timeTv.text = Util.beautifyTime(transaction.time)

        dialog.show()
    }

}