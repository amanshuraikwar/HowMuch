package io.github.amanshuraikwar.playground.ui.widget

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.amanshuraikwar.playground.R
import kotlinx.android.synthetic.main.item_user_info.*
import java.io.Serializable

open class SettingsBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var name: String
    private lateinit var photoUrl: String
    private lateinit var email: String
    private lateinit var onSignOutClicked: (DialogFragment) -> Unit

    fun init(name: String,
             photoUrl: String,
             email: String,
             onSignOutClicked: (DialogFragment) -> Unit)
            : SettingsBottomSheetDialogFragment {
        this.name = name
        this.photoUrl = photoUrl
        this.email = email
        this.onSignOutClicked = onSignOutClicked
        return this
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.item_user_info, null)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("name", name)
        outState.putString("photo_url", photoUrl)
        outState.putString("email", email)
        outState.putSerializable("on_sign_out_clicked", onSignOutClicked as Serializable)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?.let {
            name = it.getString("name")!!
            photoUrl = it.getString("photoUrl")!!
            email = it.getString("email")!!
            onSignOutClicked = it.getSerializable("on_sign_out_clicked")!! as (DialogFragment) -> Unit
        }
    }

    private fun init() {
        Glide.with(this).load(photoUrl).into(profilePicIv)
        nameTv.text = name
        emailTv.text = email
        signOutBtn.setOnClickListener { onSignOutClicked.invoke(this) }
    }
}
