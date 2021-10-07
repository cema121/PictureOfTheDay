package geekbarains.material.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import geekbarains.material.R
import geekbarains.material.util.SharedPref
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint


open class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private fun constToStyle(const: Int): Int {
        return when (const) {
            0 -> R.style.AppTheme_MarsTheme
            1 -> R.style.AppTheme_MoonTheme
            2 -> R.style.AppTheme
            else -> 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settings = SharedPref(this).loadSettings()
        setTheme(constToStyle(settings.themeId))
        setContentView(R.layout.main_activity)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setupWithNavController(navController)
    }
}