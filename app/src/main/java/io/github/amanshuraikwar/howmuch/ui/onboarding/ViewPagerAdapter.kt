package io.github.amanshuraikwar.howmuch.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fragmentManager) {

    private val fragmentList = mutableListOf<androidx.fragment.app.Fragment>()

    override fun getItem(i: Int): androidx.fragment.app.Fragment {
        return fragmentList[i]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFragment(fragment: androidx.fragment.app.Fragment) {
        fragmentList.add(fragment)
    }
}