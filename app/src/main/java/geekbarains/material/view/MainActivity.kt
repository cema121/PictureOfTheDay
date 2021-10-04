package geekbarains.material.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import geekbarains.material.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(ThemeHolder.theme)

        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_THEME,ThemeHolder.theme)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        ThemeHolder.theme = savedInstanceState.getInt(KEY_CURRENT_THEME)
    }

    companion object {
        const val KEY_CURRENT_THEME = "current_theme"
    }

    object ThemeHolder {
        var theme = R.style.AppTheme_MoonTheme
    }
}
