package org.kn.kotlin.timto.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import org.kn.kotlin.timto.fragment.PagerFirstImageFragment
import org.kn.kotlin.timto.fragment.PagerSecondImageFragment
import org.kn.kotlin.timto.fragment.PagerThreeImageFragment

class ImageFragmentPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PagerFirstImageFragment.newInstance()
            1 -> PagerSecondImageFragment.newInstance()
            2 -> PagerThreeImageFragment.newInstance()
            else -> PagerFirstImageFragment.newInstance()
        }
    }

    override fun getCount() = 3
}