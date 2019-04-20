package io.github.amanshuraikwar.howmuch.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.di.ActivityContext
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseFragment
import io.github.amanshuraikwar.howmuch.ui.editcategories.EditCategoriesActivity
import io.github.amanshuraikwar.howmuch.ui.editwallets.EditWalletsActivity
import io.github.amanshuraikwar.howmuch.ui.history.activity.HistoryActivity
import io.github.amanshuraikwar.howmuch.ui.list.ListItemTypeFactory
import io.github.amanshuraikwar.multiitemlistadapter.ListItem
import io.github.amanshuraikwar.multiitemlistadapter.MultiItemListAdapter
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment
@Inject constructor(): BaseFragment<ProfileContract.View, ProfileContract.Presenter>(), ProfileContract.View {

    private var adapter: MultiItemListAdapter<*>? = null

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, null)
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
    }

    override fun submitList(list: List<ListItem<*, *>>) {
        adapter?.submitList(list)
    }

    override fun initiateSignOut() {
        googleSignInClient.signOut()
    }

    override fun showSignOutAlertDialog() {
        AlertDialog
                .Builder(activity)
                .setMessage(R.string.sign_out_message)
                .setNegativeButton("Sign Out") {
                    dialog, _ ->
                    presenter.onSignOutClicked()
                    dialog.dismiss()
                }
                .setPositiveButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .setCancelable(true)
                .show()
    }

    override fun startHistoryActivity() {
        startActivity(Intent(activity, HistoryActivity::class.java))
    }

    override fun startWalletsActivity() {
        startActivity(Intent(activity, EditWalletsActivity::class.java))
    }

    override fun startCategoriesActivity() {
        startActivity(Intent(activity, EditCategoriesActivity::class.java))
    }

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun showSnackbar(message: String) {

    }

    override fun showLoading(message: String) {
        pb.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pb.visibility = View.GONE
    }

    override fun showError(message: String) {
        showToast(message)
    }

    @Module
    abstract class DiModule {

        @Binds
        abstract fun presenter(presenter: ProfileContract.ProfilePresenter): ProfileContract.Presenter
    }

    @Module
    class DiProvides {

        @Provides
        @ActivityContext
        fun activity(a: ProfileFragment): AppCompatActivity {
            return a.activity
        }
    }
}