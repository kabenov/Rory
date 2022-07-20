package kz.example.myapp.content

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kz.example.myapp.R
import kz.example.myapp.authorization.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fullnameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var exitImageView: ImageView

    private lateinit var email: String
    private lateinit var firstname: String
    private lateinit var lastname: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        onBind()

        bottomNavigationView.selectedItemId = R.id.profile_menu

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_page_menu -> {startActivity(Intent(this, HomePageActivity::class.java))}
                R.id.search_menu -> {startActivity(Intent(this, SearchActivity::class.java))}
                //R.id.likes_menu -> {startActivity(Intent(this, LikesActivity::class.java))}
                R.id.profile_menu -> {}
            }
            true
        }

        exitImageView.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun onBind() {
        bottomNavigationView = findViewById(R.id.profile_activity_bottom_navigation_view)
        fullnameTextView = findViewById(R.id.text_view_profile_name)
        emailTextView = findViewById(R.id.email_profile)
        exitImageView = findViewById(R.id.profile_exit)

        getSavedProfileInfo()

        fullnameTextView.text = (firstname + " " + lastname)
        emailTextView.text = email

    }

    private fun getSavedProfileInfo() {
        val sharedPreferences = getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        firstname = sharedPreferences.getString(PROFILE_FIRSTNAME_KEY, "Anonynous").toString()
        lastname = sharedPreferences.getString(PROFILE_LASTNAME_KEY, "Anonynous").toString()
        email = sharedPreferences.getString(USERNAME_KEY, "Anonynous").toString()
    }
}