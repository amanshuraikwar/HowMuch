package io.github.amanshuraikwar.howmuch.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.amanshuraikwar.howmuch.R
import kotlinx.android.synthetic.main.dialog_sign_out.view.*

class SignOutDialogFragment(val onConfirmClicked: () -> Unit)
    : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_sign_out, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.actionBtn.setOnClickListener {
            onConfirmClicked.invoke()
            this@SignOutDialogFragment.dismiss()
        }
    }
}