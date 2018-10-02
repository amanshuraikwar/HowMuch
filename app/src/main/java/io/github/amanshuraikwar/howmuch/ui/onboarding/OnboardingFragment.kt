package io.github.amanshuraikwar.howmuch.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.model.Expense
import io.github.amanshuraikwar.howmuch.ui.base.BaseFragment
import io.github.amanshuraikwar.howmuch.ui.createsheet.CreateSheetFragment
import io.github.amanshuraikwar.howmuch.ui.signin.SignInFragment
import io.github.amanshuraikwar.howmuch.util.Util
import kotlinx.android.synthetic.main.fragment_add_expense.*
import kotlinx.android.synthetic.main.fragment_onboarding.*
import kotlinx.android.synthetic.main.layout_loading_overlay.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class OnboardingFragment @Inject constructor()
    : BaseFragment<OnboardingContract.View, OnboardingContract.Presenter>(), OnboardingContract.View {

    private val TAG = Util.getTag(this)

    @Inject
    lateinit var signInFragment: SignInFragment

    @Inject
    lateinit var createSheetFragment: CreateSheetFragment

    private lateinit var adapter: ViewPagerAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_onboarding, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: called")
        signInFragment.onActivityResult(requestCode, resultCode, data)
    }

    private fun init() {

        viewPager.addOnPageChangeListener(
                object : ViewPager.OnPageChangeListener {

                    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                    }

                    override fun onPageScrollStateChanged(p0: Int) {

                    }

                    override fun onPageSelected(i: Int) {
                        (adapter.getItem(i) as OnboardingScreen).selected()
                    }
                }
        )
        viewPager.setOnTouchListener(null)

        adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(signInFragment)
        adapter.addFragment(createSheetFragment)

        viewPager.adapter = adapter
        viewPager.currentItem = 0
        (adapter.getItem(0) as OnboardingScreen).selected()
    }

    override fun setCurPage(position: Int) {
        viewPager.currentItem = position
    }
}