package geekbarains.material.view.planets

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


private const val EARTH_FRAGMENT = 0
private const val MARS_FRAGMENT = 1

class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager) {

    private val fragments = arrayOf(EarthFragment(), MarsFragment())

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> fragments[EARTH_FRAGMENT]
            1 -> fragments[MARS_FRAGMENT]
            else -> fragments[EARTH_FRAGMENT]
        }
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }
}