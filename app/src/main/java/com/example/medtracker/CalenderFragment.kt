package com.example.medtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_calender.*


class CalenderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_calender, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit {
    super.onViewCreated(view, savedInstanceState)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val fragmentAdapter = fragmentManager?.let { CalenderAdapter(it) }
        viewPager.adapter = fragmentAdapter

        tabLayout.setupWithViewPager(viewPager)

    }


}





