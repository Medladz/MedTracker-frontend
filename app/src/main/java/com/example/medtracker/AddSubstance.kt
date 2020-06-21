package com.example.medtracker

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_new_substance.floatingActionButton
import kotlinx.android.synthetic.main.add_substance.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddSubstance : AppCompatActivity(){
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.add_substance)

            val sharedPreferences = getSharedPreferences("Token", 0)
            val apiToken = sharedPreferences.getString("Token", null)

            val items = listOf("mg", "ml")
            val adapter = ArrayAdapter(this, R.layout.list_item, items )
            (unitLay.editText as? AutoCompleteTextView)?.setAdapter(adapter)

            // krijg de title van de adapter met een intent call TODO kan dit cleaner?
            val Id = intent.getStringExtra(CustomViewHolder.SubstanceIdKey)

            val name = findViewById<TextView>(R.id.Substance)
            name.text = intent.getStringExtra(CustomViewHolder.SubstanceNameKey)
            val image = findViewById<ImageView>(R.id.PreviewImage)
            Picasso.with(this).load(intent.getStringExtra(CustomViewHolder.SubstanceImageKey)).into(image)


            AddSubstance.setOnClickListener {
                if (bodyTitle.text.toString() != "" || q.text.toString() != "" || unit.text.toString() != "") { //TODO check q also for 000.00
                    if (apiToken != null) {
                        postJson(apiToken, Id)
                    } else {
                        val intent = Intent(this, LogActivity::class.java).apply {}
                        startActivity(intent)
                        finish()
                    }
                } else {
                    println(bodyTitle.text.toString() + "-" + q.text.toString() + "-" + unit.text.toString())
                    Toast.makeText(this, "You have not entered everything", Toast.LENGTH_LONG)
                        .show()
                }
            }



            floatingActionButton.setOnClickListener {
                val intent = Intent(this, NewSubstance::class.java)
                startActivity(intent)
            }
        }

    fun postJson(t : String, Id: String) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter)
        val bodyTitle = bodyTitle.text.toString()
        val note = note.text.toString().replace("\n", "\\n")
        val quantity = q.text.toString().toInt()
        val measurementUnit = unit.text.toString()
        val id = Id.toInt()

        val body = "{\"title\":\"$bodyTitle\",\"note\":\"$note\", \"quantity\":\"$quantity\",\"measurementUnit\":\"$measurementUnit\",\"consumedAt\":\"$formatted\",\"drugID\":$id}"
        //setting up the request
        Thread(Runnable {
            Fuel.post("http://83.87.187.173:8080/agendaEntries")
                .jsonBody(body)
                .authentication()
                .bearer(t)
                .also { println(it) }
                .response { result ->

                    when (result) {
                        is Result.Success -> {
                            println(result.value)
                            runOnUiThread {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        is Result.Failure -> {
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                                .show()
                            println(result)
                        }
                    }
                }
        }).start()
    }
}

