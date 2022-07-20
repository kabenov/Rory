package kz.example.myapp.content

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import kz.example.myapp.R

class LikesActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likes)

        onBind()

        //bottomNavigationView.selectedItemId = R.id.likes_menu

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_page_menu -> {startActivity(Intent(this, HomePageActivity::class.java))}
                R.id.search_menu -> {startActivity(Intent(this, SearchActivity::class.java))}
                //R.id.likes_menu -> {}
                R.id.profile_menu -> {startActivity(Intent(this, ProfileActivity::class.java))}
            }
            true
        }
    }

    private fun onBind() {
        bottomNavigationView = findViewById(R.id.likes_activity_bottom_navigation_view)
    }
}