package com.example.medtracker

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_register.*
import java.security.MessageDigest

class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_register, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setClickListenUsername() //To set the listener for the username
        setClickListenEmail() //To set the listener for the email
        setClickListenPassword() //To set the Listener for the password
        setClickListenDate() //To set the listener for the date picker
        setClickRegister() //To set the listener for the registerButton
    }


    fun showError(editText: TextInputLayout, message: String) { // insert a layout and a message to tell the user an error
        editText.error = message
        editText.requestFocus()
    }

    private fun setClickListenUsername() {
        username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {} //Not used but mandatory
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} //Not used but mandatory

            override fun afterTextChanged(s: Editable?) {
                val content = username.text.toString()
                when {
                    content.length > 254 -> {
                        showError(usernameLay, "Your name is too long")
                    }
                    content.isEmpty() -> {
                        showError(usernameLay, "You need to fill in an username")
                    }
                    else -> usernameLay.error = null
                }
            }
        })
    }

    private fun setClickListenEmail() {
        email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {} //Not used but mandatory
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} //Not used but mandatory

            override fun afterTextChanged(s: Editable?) {
                val content = email.text.toString().trim()
                when {
                    content.length > 254 -> {
                        showError(emailLay, "Your email is too long")
                    }
                    content.isEmpty() -> {
                        showError(emailLay, "You need to fill in an email")
                    }
                    else -> emailLay.error = null
                }
            }
        })
    }

    private fun setClickListenPassword() {
        val set = ConstraintSet() // setting the Constraintset for the layout
        set.clone(registerLay) // cloning the current layout

        password.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && passwordLay.error != null) { // when the passwordfield lost focus and has an error: focus
                v.requestFocus()
            } else if (!hasFocus && passwordLay.error == null) { // when the password field lost focus en does not have an error: set the strenghtmeter enz to gone
                set.clear(passwordLay.id, ConstraintSet.TOP)
                set.connect(passwordLay.id, ConstraintSet.TOP, emailLay.id, ConstraintSet.BOTTOM)
                set.applyTo(registerLay)
                password_strength.visibility = View.GONE
                progressBar.visibility = View.GONE
                login_instructions.visibility = View.GONE
            }

            if (hasFocus) { // when the password field is in focus, set the strenghtmeter enz visible
                set.clear(passwordLay.id, ConstraintSet.TOP)
                set.connect(passwordLay.id, ConstraintSet.TOP, login_instructions.id, ConstraintSet.BOTTOM)
                set.applyTo(registerLay)
                password_strength.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
                login_instructions.visibility = View.VISIBLE
            }
        }

        password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {} //Not used but mandatory
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} //Not used but mandatory

            override fun afterTextChanged(s: Editable?) { //when changing the password, it updates the strenght
               updatePasswordStrengthView(s.toString())
            }
        })
    }

    private fun setClickListenDate() { //sets the listener for the datepicker used to get the users day of birth
        val datePickerText = datePicker
        val c: Calendar = Calendar.getInstance() // the date of today to start the calender from
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        datePicker.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                activity?.let { it1 ->
                    DatePickerDialog(
                        it1,
                        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                            val newMonthOfYear = monthOfYear + 1
                            datePickerText.setText(("$year-$newMonthOfYear-$dayOfMonth"))
                        },
                        year,
                        month,
                        day
                    )
                }?.show()
            }
        }

        datePicker.setOnClickListener {
            //onClickListener voor het invullen van een datum
            activity?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        val newMonthOfYear = monthOfYear + 1
                        datePickerText.setText(("$year-$newMonthOfYear-$dayOfMonth"))
                    },
                    year,
                    month,
                    day
                )
            }?.show()
        }
    }

    private fun updatePasswordStrengthView(password: String) { //function for the strenght of the password

        val progressBar = progressBar as ProgressBar
        val strengthView = password_strength as TextView
        if (TextView.VISIBLE != strengthView.visibility)
            return

        if (TextUtils.isEmpty(password)) {
            strengthView.text = ""
            progressBar.progress = 0
            return
        }

        val str = PasswordStrength.calculateStrength(password)
        strengthView.text = str.getText(this)
        strengthView.setTextColor(str.color)

        progressBar.progressDrawable.setColorFilter(str.color, android.graphics.PorterDuff.Mode.SRC_IN)
        when {
            str.getText(this) == "Weak" -> {
                progressBar.progress = 25
            }
            str.getText(this) == "Medium" -> {
                progressBar.progress = 50
            }
            str.getText(this) == "Strong" -> {
                progressBar.progress = 75
            }
            else -> {
                progressBar.progress = 100
            }
        }
    }

    private fun setClickRegister() { // function to checks all the inputs, before trying to post
        registerButton.setOnClickListener {
            val content = email.text.toString().trim()
            if (usernameLay.error != null || username.text!!.isEmpty()) {
                username.requestFocus()
            } else if (emailLay.error != null || email.text!!.isEmpty()) {
                email.requestFocus()
            } else if (passwordLay.error != null || password.text!!.isEmpty()) {
                password.requestFocus()
            } else if (datePicker.text!!.isEmpty()) {
                activity?.currentFocus?.clearFocus()
                datePicker.performClick()
            } else if (!TextUtils.isEmpty(content) && !android.util.Patterns.EMAIL_ADDRESS.matcher(content).matches()) {
                showError(emailLay, "Your email is invalid")
            } else post()
        }
    }

    private fun post() { //post function for posting the user
        //getting text from the form
        var username = username.text.toString()
        var email = email.text.toString()
        var password = password.text.toString().sha512() //already hashed
        var dateOfBirth = datePicker.text.toString()
        var registerFormBody = "{\"username\":\"$username\",\"email\":\"$email\", \"password\":\"$password\",\"birthday\":\"$dateOfBirth\"}"

        //setting up the request
        Thread(Runnable {
            Fuel.post("http://83.87.187.173:8080/users")
                .jsonBody(registerFormBody)
                .also { println(it) }
                .response { result ->
                    when (result) {
                        is Result.Success -> {
                            activity?.runOnUiThread {
                                loginPost(email, password)
                            }
                        }
                        is Result.Failure -> {
                            activity?.runOnUiThread {
                                showError(emailLay, "Your email was already found in our database")
                            }
                        }
                    }
                }
        }).start()
    }

    private fun String.sha512(): String { //hashing extension
        return this.hashWithAlgorithm("SHA-512")
    }

    private fun String.hashWithAlgorithm(algorithm: String): String { //hashing. Use like this: myString.sha512()
        val digest = MessageDigest.getInstance(algorithm)
        val bytes = digest.digest(this.toByteArray(Charsets.UTF_8))
        return bytes.fold("", { str, it -> str + "%02x".format(it) })
    }

    private fun loginPost(email: String, password: String) {
        val registerFormBody = "{\"email\":\"$email\",\"password\":\"$password\"}"

        //setting up the request
        Thread(Runnable {
            Fuel.post("http://83.87.187.173:8080/users/login")

                .jsonBody(registerFormBody)
                .also { println(it) }
                .responseObject(LoginFragment.User.Deserializer()) { request, response, result ->

                    val (user, err) = result
                    when (result) {
                        is Result.Success -> {
                            activity?.runOnUiThread {
                                saveToken(user!!.data) //save the token to sharedpreferences
                                startMain() //start the main activity
                            }
                        }
                        is Result.Failure -> {
                            println(result)
                            activity?.runOnUiThread {
                                Toast.makeText(
                                    context,
                                    "Your email or password is incorrect",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
        }).start()
    }

    private fun saveToken(token: String) { // function for saving the API token in the shared preferences
        val sharedPreferences = this.activity?.getSharedPreferences("Token", 0)
        val editor = sharedPreferences?.edit()
        editor?.putString("Token", token)
        editor?.apply()
    }


    private fun startMain() { // function to start the main activity
        val intent = Intent(this.activity, MainActivity::class.java).apply {
        }
        startActivity(intent)
    }

}


