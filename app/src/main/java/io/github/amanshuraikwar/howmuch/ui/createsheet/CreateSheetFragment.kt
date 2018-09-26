package io.github.amanshuraikwar.howmuch.ui.createsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.ui.base.BaseFragment
import javax.inject.Inject

class CreateSheetFragment @Inject constructor()
    : BaseFragment<CreateSheetContract.View, CreateSheetContract.Presenter>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_onboarding_create_sheet, null)
    }
}