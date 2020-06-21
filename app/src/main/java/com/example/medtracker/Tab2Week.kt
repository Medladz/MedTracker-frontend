package com.example.medtracker

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
        val editor = sharedPreferences?.edit()
        editor?.putString("Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjb20ubWVkdHJhY2tlciIsInVzZXJJZCI6Mn0.o9moOSmAis83H0iFEzKa5FNuyHAKeGQB1_2KFRkoxfx5m6N_qj8oQcIOuXGaSQp2-Me0NWm9-8e3rRRGK3gQcw")
        editor?.apply()


        val apiToken = sharedPreferences?.getString("Token", null)
        val weekDayArray = arrayListOf(Monday_date_text,Tuesday_date_text, Wednesday_date_text, Thursday_date_text, Friday_date_text ,Saturday_date_text ,Sunday_date_text)
        var weekLayout =   arrayListOf(Monday_layout,Tuesday_layout, Wednesday_layout, Thursday_layout, Friday_layout ,Saturday_layout ,Sunday_layout)
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
        var agendaArray = JSONArray()
        //setting up the request
        FuelManager.instance.removeAllRequestInterceptors()
        if (apiToken != null) {
            Fuel.get("http://192.168.1.2:8080/agendaEntries?include=drug") //TODO make this request to server
                .header("Authorization", "Bearer " + apiToken)
                .responseObject(CalenderDeserializer()) { result ->
                        when (result) {
                            is Result.Success -> {
                                println(result.value)

                            }
                            is Result.Failure -> {
                                println(result)
                            }
                        }

//                        var i = 0
//                        while (agendaArray.length() > i) { //IT here is ONE agenda ENTRY
//                            var filteredArray = mutableMapOf<String,String>()
//                            var filteredDrugArray = mutableMapOf<String,String>()
//                            var entryArray = agendaArray.getJSONObject(i).get("consumedAt").toString().replace("{", "").replace("}","").split(",")
//                            var drugArray = agendaArray.getJSONObject(i).get("drug").toString().replaceFirst("{", "").replaceFirst("}","").replaceFirst("{", "").replaceFirst("}","").split(",")
//                            drugArray.forEach{
//                                var drugTempArr = it.replace("\"", "").split(":")
//                                if(drugTempArr[0] == "name" && drugTempArr[1] != "null" ){
//                                    filteredDrugArray[drugTempArr[0]] = drugTempArr[1]
//                                }
//                            }
//                            entryArray.forEach {
//                                var entryTempArr = it.replace("\"", "").split(":")
//                                filteredArray[entryTempArr[0]] = entryTempArr[1]
//                            }
//                            if(weekDateArray.containsKey(SimpleDateFormat("yyyy-MM-dd").format(filteredArray["millis"]!!.toLong()))){
//                                activity!!.runOnUiThread {
//
//                                    val parser: XmlPullParser = resources.getXml(R.xml.eventstyle)
//                                    try {
//                                        parser.next()
//                                        parser.nextTag()
//                                    } catch (e: Exception) {
//                                        e.printStackTrace()
//                                    }
//
//                                    val attr: AttributeSet = Xml.asAttributeSet(parser)
//                                    var singleEventView = TextView(activity,attr)
//                                    singleEventView.text = filteredDrugArray.get("name")
//                                    singleEventView.setId(i)
//                                    var weekDay: LinearLayout? = weekLayoutArray[weekDateArray[SimpleDateFormat("yyyy-MM-dd").format(filteredArray["millis"]!!.toLong())]]
//                                    weekDay!!.addView(singleEventView)
//                                    println(singleEventView)
//
//                                }
//                            }


//                            i++
//                        }
//                    }, failure = { error ->
//                        Log.e("Failure", error.toString())
//                    })

                }
        }


    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

    }
}

fun fillWeek(agendaArray: JSONArray, weekDateArray: MutableMap<String, TextView>){

}



