package com.example.medtracker


import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_substance.*
import kotlinx.android.synthetic.main.fragment_substance.*
import okhttp3.*
import java.io.IOException

class NewSubstance : FragmentActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_substance)

        Substance_recyclerview.layoutManager = LinearLayoutManager(this)

        floating_action_button.setOnClickListener{
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        }

        fetchJson()
    }
    fun fetchJson(){
        val apiUrl = "http://192.168.2.19:8080/drugs"
        val request = Request.Builder().url(apiUrl).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()

                val drugFeed = gson.fromJson(body, DrugFeed::class.java)
                runOnUiThread {
                    Substance_recyclerview.adapter = SubstanceAdapter(drugFeed)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("failed to execute")
            }
        })
    }
}


