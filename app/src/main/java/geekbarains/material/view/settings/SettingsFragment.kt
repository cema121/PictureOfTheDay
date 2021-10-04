package geekbarains.material.view.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import geekbarains.material.databinding.FragmentSettingsBinding
import com.google.android.material.radiobutton.MaterialRadioButton
import geekbarains.material.R
import geekbarains.material.view.MainActivity

open class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioButtonSetOnClickListener(binding.moonThemeRadioButton, R.style.AppTheme_MoonTheme)
        radioButtonSetOnClickListener(binding.marsThemeRadioButton, R.style.AppTheme_MarsTheme)
        radioButtonSetOnClickListener(binding.defaultThemeRadioButton, R.style.AppTheme)
    }

    private fun radioButtonSetOnClickListener(button: MaterialRadioButton, theme: Int) {
        button.setOnClickListener {
            MainActivity.ThemeHolder.theme = theme
            requireActivity().recreate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}