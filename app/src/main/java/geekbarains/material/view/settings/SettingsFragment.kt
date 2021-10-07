package geekbarains.material.view.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import geekbarains.material.R
import geekbarains.material.model.Settings
import kotlinx.android.synthetic.main.fragment_settings.*
import geekbarains.material.util.SharedPref

class SettingsFragment : Fragment() {

    private var checkTheme = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        checkTheme = true
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkTheme = false

        chipMars.setOnClickListener {
            setTheme(R.style.AppTheme_MarsTheme)
        }

        chipMoon.setOnClickListener {
            setTheme(R.style.AppTheme_MoonTheme)
        }

        chipEarth.setOnClickListener {
            setTheme(R.style.AppTheme)
        }
    }

    private fun styleToConst(style: Int): Int {
        return when (style) {
            R.style.AppTheme_MarsTheme -> 0
            R.style.AppTheme_MoonTheme -> 1
            R.style.AppTheme -> 2
            else -> 0
        }
    }

    private fun setTheme(style: Int) {
        SharedPref(requireContext()).saveSettings(
            Settings(
                styleToConst(
                    style
                )
            )
        )
        requireActivity().setTheme(style)
        requireActivity().recreate()
    }
}