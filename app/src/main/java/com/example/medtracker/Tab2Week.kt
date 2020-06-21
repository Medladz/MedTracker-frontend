package com.example.medtracker

import android.app.Activity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.util.Xml
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.json.responseJson
import kotlinx.android.synthetic.main.calender_week.*
import org.json.JSONArray
import org.xmlpull.v1.XmlPullParser
import java.text.SimpleDateFormat
import java.util.*
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class Tab2Week : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.calender_week, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val sharedPreferences = activity?.getSharedPreferences("Token", 0)
        val apiToken = sharedPreferences?.getString("Token", null)

        val weekDayArray = arrayListOf(Monday_date_text,Tuesday_date_text, Wednesday_date_text, Thursday_date_text, Friday_date_text ,Saturday_date_text ,Sunday_date_text)
        var weekLayout =   arrayListOf(lLayMon,lLayTue, lLayWed, lLayThu, lLayFri ,lLaySat ,lLaySun)
        val weekDateArray = mutableMapOf<String,TextView>()
        val weekLayoutArray = mutableMapOf<TextView, LinearLayout>()
        var currentWeek: Calendar = Calendar.getInstance()
        currentWeek.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)
        var layoutInt = 0
        weekDayArray.forEach{
                var weekDay = SimpleDateFormat("yyyy-MM-dd").format(currentWeek.getTimeInMillis())
            weekDateArray[weekDay] = it
            weekLayoutArray[it] = weekLayout[layoutInt]
            it.setText(SimpleDateFormat("dd-MM").format(currentWeek.getTimeInMillis()))
            currentWeek.add(Calendar.DAY_OF_YEAR, 1)
            layoutInt++
        }


        var r = Random
        //setting up the request
        FuelManager.instance.removeAllRequestInterceptors()
        if (apiToken != null) {
            Fuel.get("http://192.168.1.2:8080/agendaEntries?include=drug") //TODO make this request to server
                .header("Authorization", "Bearer " + apiToken)
                .responseObject(CalenderDeserializer()) { result ->
                        when (result) {
                            is Result.Success -> {
                                result.value.forEach{
                                    if(weekDateArray.containsKey(SimpleDateFormat("yyyy-MM-dd").format(it.consumedAt.millis))){
                                    activity!!.runOnUiThread {

                                        val parser: XmlPullParser = resources.getXml(R.xml.eventstyle)
                                        val attr: AttributeSet = Xml.asAttributeSet(parser)
                                        var singleEventView = TextView(activity,attr)
                                        singleEventView.text = it.title
                                        singleEventView.setId(r.nextInt((100-10)+1)+10)
                                        var weekDay = weekLayoutArray[weekDateArray[SimpleDateFormat("yyyy-MM-dd").format(it.consumedAt.millis)]]

                                        weekDay!!.addView(singleEventView)

                                        weekDay.setOnClickListener {
                                            println(it)
                                            activity!!.layoutInflater!!.inflate(R.layout.calender_day, container, false)
                                        }
                                    }
                                }
                                }

                            }
                            is Result.Failure -> {
                                println(result)
                            }
                        }
                }
        }


    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

    }
}

