package kz.example.myapp.authorization

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kz.example.myapp.R
import kz.example.myapp.content.HomePageActivity
import kz.example.myapp.presentation.common.AbstractTextWatcher

const val USERNAME_KEY: String = "username"
const val NAME_SHARED_PREFERENCES: String = "authorizationSP"
const val PROFILE_FIRSTNAME_KEY: String = "Firstname"
const val PROFILE_LASTNAME_KEY: String = "Lastname"

class AuthorizationActivity: AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var firstnameEditText: EditText
    private lateinit var lastnameEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var toLoginTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_authorization)

        onBind()
        listenerEditTexts()

        submitButton.setOnClickListener {
            val email = emailEditText.text.toString().trim(){it <= ' '}
            val password = passwordEditText.text.toString().trim(){it <= ' '}
            val firstname = firstnameEditText.text.toString().trim(){it <= ' '}
            val lastname = lastnameEditText.text.toString().trim(){it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result!!.user!!

                        Toast.makeText(this, "You are registered successfully", Toast.LENGTH_SHORT).show()

                        saveProfileInfo(firstname, lastname, email)
                        toSecondActivity(firebaseUser, email)
                    }
                    else {
                        Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }

        toLoginTextView.setOnClickListener{
            onBackPressed()
        }
    }

    private fun onBind() {
        emailEditText = findViewById(R.id.activity_authorization_edit_text_email)
        passwordEditText = findViewById(R.id.activity_authorization_edit_text_password)
        firstnameEditText = findViewById(R.id.activity_authorization_edit_text_firstname)
        lastnameEditText = findViewById(R.id.activity_authorization_edit_text_lastname)
        submitButton = findViewById(R.id.activity_authorization_button_submit)
        toLoginTextView = findViewById(R.id.activity_authorization_text_view_to_login)
    }

    private fun listenerEditTexts() {
        var isEmailEditTextEmpty = true
        var isPasswordEditTextEmpty = true

        emailEditText.addTextChangedListener(object: AbstractTextWatcher() {
            override fun afterTextChanged(p0: Editable?) {
                isEmailEditTextEmpty = p0.toString().isBlank() // isBlank() как isEmpty() но еще и проверяет на наличие пробелов

                if (!isEmailEditTextEmpty && !isPasswordEditTextEmpty) {
                    submitButton.isEnabled = !isEmailEditTextEmpty
                }
            }
        })

        passwordEditText.addTextChangedListener(object: AbstractTextWatcher() {
            override fun afterTextChanged(p0: Editable?) {
                isPasswordEditTextEmpty = p0.toString().isBlank() // isBlank() как isEmpty() но еще и проверяет на наличие пробелов

                if (!isEmailEditTextEmpty && !isPasswordEditTextEmpty) {
                    submitButton.isEnabled = !isPasswordEditTextEmpty
                }
            }
        })
    }

    private fun toSecondActivity(firebaseUser: FirebaseUser, email: String){
        val intent = Intent(this, HomePageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        intent.putExtra("user_id", firebaseUser.uid)
        intent.putExtra("email_id", email)

        startActivity(intent)
        finish()
    }

//    private fun isUsernameSaved(): Boolean {
//        val sharedPreferences = getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
//
//        return sharedPreferences.contains(USERNAME_KEY)
//    }
//
    private fun saveProfileInfo(firstname: String, lastname: String, email: String) {
        val sharedPreferences = getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(PROFILE_FIRSTNAME_KEY, firstname).apply()
        editor.putString(PROFILE_LASTNAME_KEY, lastname).apply()
        editor.putString(USERNAME_KEY, email).apply()
    }

}