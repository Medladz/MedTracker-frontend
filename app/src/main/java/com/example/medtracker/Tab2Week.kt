package com.example.medtracker

import android.os.Bundle
import android.util.AttributeSet
import android.util.Xml
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import kotlinx.android.synthetic.main.calender_week.*
import org.xmlpull.v1.XmlPullParser
import java.text.SimpleDateFormat
import java.util.*
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

        val weekDayArray = arrayListOf(MondayDate,TuesdayDate, WednesdayDate, ThursdayDate, FridayDate ,SaturdayDate ,SundayDate)
        var weekLayout =   arrayListOf(lLayMon,lLayTue, lLayWed, lLayThu, lLayFri ,lLaySat ,lLaySun)
        val weekDateArray = mutableMapOf<String,TextView>()
        val weekLayoutArray = mutableMapOf<TextView, LinearLayout>()
        var currentWeek: Calendar = Calendar.getInstance()

        //Get the API login Token to use as authorization on the Fuel request
        val sharedPreferences = activity?.getSharedPreferences("Token", 0)
        val apiToken = sharedPreferences?.getString("Token", null)

        //Set the currentweek to this week/start at monday
        currentWeek.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)

        //Gets the current week and inserts the dates into an array and into the view
        var layoutWeekDay = 0
        weekDayArray.forEach{
            var weekDay = SimpleDateFormat("yyyy-MM-dd").format(currentWeek.getTimeInMillis())
            weekDateArray[weekDay] = it
            weekLayoutArray[it] = weekLayout[layoutWeekDay]
            it.setText(SimpleDateFormat("dd-MM").format(currentWeek.getTimeInMillis()))
            currentWeek.add(Calendar.DAY_OF_YEAR, 1)
            layoutWeekDay++
        }


        //setting up the request
        FuelManager.instance.removeAllRequestInterceptors()

        Fuel.get("http://83.87.187.173:8080/agendaEntries?include=drug") //TODO make this request to server
            .header("Authorization", "Bearer " + apiToken)
            .responseObject(CalenderDeserializer()) { result ->
                when (result) {
                    is Result.Success -> {
                        result.value.forEach{
                            //Checks if the currentWeek has appointments
                            if(weekDateArray.containsKey(SimpleDateFormat("yyyy-MM-dd").format(it.consumedAt.millis))){
                                //Run on ui thread because otherwise you arent allowed to change view elements
                                activity!!.runOnUiThread {
                                    //Parsing the eventStyle xml so that we can reuse one layout instead of having to make 7
                                    val parser: XmlPullParser = resources.getXml(R.xml.eventstyle)
                                    try {
                                        parser.next()
                                        parser.nextTag()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    val attr: AttributeSet = Xml.asAttributeSet(parser)
                                    var singleEventView = TextView(activity,attr)
                                    singleEventView.text = it.title
                                    singleEventView.setId(Random.nextInt((100-10)+1)+10)

                                    var weekDay = weekLayoutArray[weekDateArray[SimpleDateFormat("yyyy-MM-dd").format(it.consumedAt.millis)]]
                                    weekDay!!.addView(singleEventView)
                                    weekDay.setOnClickListener {
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

