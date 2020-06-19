package com.example.medtracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val APIToken = getApiToken()

        if (APIToken == null) { //for checking the existence of the APItoken, if there is none, start LogActivity
            val intent = Intent(this, LogActivity::class.java).apply {
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        setContentView(R.layout.activity_main)
        loadFragment(Substance())
        toolbar.checkItem(R.id.navSubstances)

        toolbar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navCalender-> {
                    title=resources.getString(R.string.calender)
                    loadFragment(Calender())
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

    fun getApiToken(): String? {
        val sharedPreferences = getSharedPreferences("Token", 0)
        val token = sharedPreferences.getString("Token", null)
        return token
    }
}
