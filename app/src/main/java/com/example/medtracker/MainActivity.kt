package com.example.medtracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.calender_week.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("Token", 0)
        val APIToken = sharedPreferences.getString("Token", null)

        if (APIToken == null) { //for checking the existence of the APItoken, if there is none, start LogActivity
            val intent = Intent(this, LogActivity::class.java).apply {
            }
            startActivity(intent)
        }

        setContentView(R.layout.activity_main)
        loadFragment(Substance())
        toolbar.checkItem(R.id.navSubstances)

        toolbar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navCalender-> {
                    title= resources.getString(R.string.calender)
//                    setContentView(R.layout.fragment_calender)
                    loadFragment(CalenderFragment())

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navSubstances-> {
                    title=resources.getString(R.string.substances)
                    loadFragment(Substance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navSettings-> {
                    title=resources.getString(R.string.settings)
                    loadFragment(Settings())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }





    }
    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun BottomNavigationView.checkItem(actionId: Int) {
        menu.findItem(actionId)?.isChecked = true
    }
}
