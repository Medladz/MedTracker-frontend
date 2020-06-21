package com.example.medtracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.json.responseJson
import kotlinx.android.synthetic.main.calender_week.*
import kotlinx.android.synthetic.main.fragment_calender.*


class CalenderFragment : Fragment() {

//    var maText = Monday_text

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_calender, container, false)


//
//    override fun onCreate(){
//
//
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit {
    super.onViewCreated(view, savedInstanceState)

    //initialize your view here for use view.findViewById("your view id")
//    dayView = Monday_text
}
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val weekTextArray: List<String> = arrayListOf("Monday_text","Tuesday_text","Wednesday_text","Thurday_text","Friday_text","Saturday_text","Sunday_text")
        val weekDateArray: List<String> = arrayListOf("Monday_date_text","Tuesday_date_text")
//        var dayView: TextView = Monday_text
//        dayView =  getView().findViewByID(R.id.x)

//        dayView.get
//        getAgenda()
//        maText!!.setText("fdskfjdasjfkdasfkldjas")
//        dayView?.setText("TESSST")

        val fragmentAdapter = fragmentManager?.let { CalenderAdapter(it) }
        viewPager.adapter = fragmentAdapter

        tabLayout.setupWithViewPager(viewPager)

    }

//
//    class MyCalenderView : CalendarView(null,null,null,null ) {
//    }




}





