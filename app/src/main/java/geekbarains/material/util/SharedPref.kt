package geekbarains.material.util

import android.content.Context
import android.content.SharedPreferences
import geekbarains.material.Constant.NAME_SHARED_PREFERENCE
import geekbarains.material.R
import geekbarains.material.model.Settings


class SharedPref(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    private val contextLoc = context

    fun loadSettings(): Settings {
        return Settings(
            sharedPreferences.getInt(contextLoc.getString(R.string.ThemeId), 0)
        )
    }

    fun saveSettings(settings: Settings) {
        val editor = sharedPreferences.edit()
        editor.putInt(contextLoc.getString(R.string.ThemeId), settings.themeId)
        editor.apply()
    }
}