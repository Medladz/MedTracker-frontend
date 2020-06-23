package com.example.medtracker


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_new_substance.*
import kotlin.properties.Delegates

class NewSubstance : FragmentActivity() {
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private var y by Delegates.notNull<Int>()

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

        setScrollListener()



        addSubstanceButton.setOnClickListener {
            Toast.makeText(this, "feature coming soon", Toast.LENGTH_LONG)
                .show()
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

    fun setScrollListener() {
        Substance_recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                y =dy
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    //gone
                }
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    //visible
                    if(y <= 0 ) {
                        addSubstanceButton.visibility = View.VISIBLE
                    } else {
                        y = 0
                        addSubstanceButton.visibility = View.GONE
                    }
                }
            }
        })
    }

}