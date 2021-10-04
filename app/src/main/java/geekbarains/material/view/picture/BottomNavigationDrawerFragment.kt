package geekbarains.material.view.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import geekbarains.material.R
import geekbarains.material.view.PictureOfTheDayFragment
import geekbarains.material.view.chips.ChipsFragment
import kotlinx.android.synthetic.main.bottom_navigation_layout.*

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_navigation_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_one -> activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, PictureOfTheDayFragment())?.addToBackStack(null)?.commit()
                R.id.navigation_two -> activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, ChipsFragment())?.addToBackStack(null)?.commit()
            }
            true
        }
    }
}
