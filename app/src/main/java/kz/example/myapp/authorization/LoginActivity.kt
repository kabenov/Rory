package kz.example.myapp.authorization

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kz.example.myapp.R
import kz.example.myapp.content.HomePageActivity
import kz.example.myapp.presentation.common.AbstractTextWatcher

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var toRegisterTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        startActivity(Intent(this, HomePageActivity::class.java))

        onBind()
        listenerEditTexts()

        submitButton.setOnClickListener {
            val email = emailEditText.text.toString().trim(){it <= ' '}
            val password = passwordEditText.text.toString().trim(){it <= ' '}

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "You are logged in successfully", Toast.LENGTH_SHORT).show()

                        toSecondActivity(email)
                    }
                    else {
                        Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }


        toRegisterTextView.setOnClickListener{
            startActivity(Intent(this, AuthorizationActivity::class.java))
        }
    }

    private fun onBind() {
        emailEditText = findViewById(R.id.activity_authorization_edit_text_email)
        passwordEditText = findViewById(R.id.activity_authorization_edit_text_password)
        submitButton = findViewById(R.id.activity_authorization_button_submit)
        toRegisterTextView = findViewById(R.id.activity_authorization_text_view_to_register)
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

    private fun toSecondActivity(email: String){
        val intent = Intent(this, HomePageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
        intent.putExtra("email_id", email)

        startActivity(intent)
        finish()
    }
}