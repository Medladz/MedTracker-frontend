package com.example.medtracker

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_settings.*

class Settings : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setDeleteListener(deleteLay)
        setLogListener(logLay)
    }

    fun getApiToken(): String? {
        val sharedPreferences = context?.getSharedPreferences("Token", 0)
        val token = sharedPreferences?.getString("Token", null)
        return token
    }

    fun setLogListener(button: ConstraintLayout) {
        button.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Log out")
                .setMessage("Do you want to proceed logging out?")
                .setPositiveButton("Log out") { dialog: DialogInterface?, which: Int ->
                    deleteToken()
                    val intent = Intent(this.context, LogActivity::class.java).apply {
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    activity!!.finish()
                }
                .setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int ->
                    dialog?.cancel()
                }.show()
        }
    }

    fun setDeleteListener(button: ConstraintLayout) {
        button.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Delete account")
                .setMessage("Do you want to proceed deleting your account?")
                .setPositiveButton("Delete") { dialog: DialogInterface?, which: Int ->
                    delete()
                }
                .setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int ->
                    dialog?.cancel()
                }.show()
        }
    }

    fun delete() {
        val apiToken = getApiToken()
        Thread(Runnable {
            val (_, _, result) = Fuel.delete("http://192.168.1.4:8080/user/$apiToken") //TODO make this request to server
                .also { println(it) }
                .responseString()

            when (result) {
                is Result.Success -> {
                    activity?.runOnUiThread {
                        deleteToken()
                        val intent = Intent(this.context, LogActivity::class.java).apply {
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        activity!!.finish()
                    }
                }
                is Result.Failure -> {
                    println(result)
                    activity?.runOnUiThread {
                        Toast.makeText(context,"Something went wrong", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }).start()

    }

    private fun deleteToken() { // function for saving the API token in the shared preferences
        val sharedPreferences = this.activity?.getSharedPreferences("Token", 0)
        val editor = sharedPreferences?.edit()
        editor?.putString("Token", null)
        editor?.apply()
    }
}
