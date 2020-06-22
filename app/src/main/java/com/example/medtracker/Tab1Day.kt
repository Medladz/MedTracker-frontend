package com.example.medtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class Tab1Day : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.calender_day, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val weekLayoutArray = mutableMapOf<TextView, ConstraintLayout>()
        var currentWeek: Calendar = Calendar.getInstance()
        currentWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        var weekDay = SimpleDateFormat("MM-dd").format(currentWeek.getTimeInMillis())
    }
}