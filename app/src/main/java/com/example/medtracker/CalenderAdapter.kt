package com.example.medtracker

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentManager

class CalenderAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            else -> {
                return Tab2Week()
            }
        }
    }



    override fun getCount(): Int {
        return 1
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            else -> {
                return "Week"
            }
        }
    }
}