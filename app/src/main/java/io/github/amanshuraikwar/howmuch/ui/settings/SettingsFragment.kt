package io.github.amanshuraikwar.howmuch.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseFragment
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment
@Inject constructor(): BaseFragment<SettingsContract.View, SettingsContract.Presenter>(), SettingsContract.View {

    private var adapter: MultiItemListAdapter<*>? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {

        itemsRv.layoutManager = LinearLayoutManager(activity)

        if (adapter == null) {
            adapter = MultiItemListAdapter(activity, ListItemTypeFactory())
        }

        itemsRv.adapter = adapter

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_active_24dp)
        toolbar.setNavigationOnClickListener {
            activity.onBackPressed()
        }

        toolbar.inflateMenu(R.menu.refresh_navigation)
        toolbar.setOnMenuItemClickListener {
            presenter.onRefreshClicked()
            return@setOnMenuItemClickListener true
        }
    }

    override fun submitList(list: List<ListItem<*, *>>) {
        adapter?.submitList(list)
    }

    override fun setSyncError() {
        toolbar.menu.getItem(0).icon =
                ContextCompat.getDrawable(activity, R.drawable.ic_sync_problem_red_24dp)
    }

    override fun clearSyncError() {
        toolbar.menu.getItem(0).icon =
                ContextCompat.getDrawable(activity, R.drawable.ic_autorenew_black_24dp)
    }

    override fun initiateSendFeedback() {

        val emailIntent = Intent(
                Intent.ACTION_SENDTO,
                Uri.fromParts(
                "mailto", "amanshuraikwar.dev@gmail.com", null
                )
        )

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on app HowMuch")

        startActivity(Intent.createChooser(emailIntent, "Send feedback..."))
    }

    override fun startDecimalDialog(title: String,
                                    initVal: Double,
                                    onSuccess: (Double) -> Unit) {

        val view = activity.getLayoutInflater().inflate(R.layout.layout_setting_decimal, null) as EditText
        view.setText(initVal.toString())

        AlertDialog
                .Builder(activity)
                .setView(view)
                .setMessage(title)
                .setNegativeButton("Cancel") {
                    dialog, _ -> dialog.dismiss()
                }
                .setPositiveButton("Save") {
                    dialog, _ ->
                    onSuccess.invoke(
                            if (view.text.isNotEmpty() && view.text.toString() != ".")
                                view.text.toString().toDouble()
                            else
                                0.0
                    )
                    dialog.dismiss()
                }
                .setCancelable(true)
                .show()
    }

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun showSnackbar(message: String) {

    }

    override fun showLoading(message: String) {
        pb.visibility = VISIBLE
    }

    override fun hideLoading() {
        pb.visibility = GONE
    }

    override fun showError(message: String) {
        showToast(message)
    }

    @Module
    abstract class DiModule {
        @Binds
        abstract fun presenter(presenter: SettingsContract.PresenterImpl): SettingsContract.Presenter
    }
}