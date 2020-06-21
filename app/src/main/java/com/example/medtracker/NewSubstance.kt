package com.example.medtracker


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_new_substance.*
import java.util.*

class NewSubstance : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_substance)

        val sharedPreferences = getSharedPreferences("Token", 0)
        val apiToken = sharedPreferences.getString("Token", null)

        Substance_recyclerview.layoutManager = LinearLayoutManager(this)

        floatingActionButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        }

        if (apiToken != null) {
            fetchJson(apiToken)
        } else {
            val intent = Intent(this, LogActivity::class.java).apply {
            }
            startActivity(intent)
            finish()
        }
    }
    fun fetchJson(t : String) {
        //setting up the request
        Thread(Runnable {
            Fuel.get("http://83.87.187.173:8080/drugs?include=components,brands,sources,containers&withVerified=true")
                .authentication()
                .bearer(t)
                .also { println(it) }
                .responseObject(Deserializer()) { result ->

                    when (result) {
                        is Result.Success -> {
                            println(result)
                            runOnUiThread {
                                Substance_recyclerview.adapter = SubstanceAdapter(result.value)
                            }
                        }
                        is Result.Failure -> {
                            println(result)
                        }
                    }
                }
        }).start()
    }

}