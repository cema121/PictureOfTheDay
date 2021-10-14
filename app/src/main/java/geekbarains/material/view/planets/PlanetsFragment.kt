package geekbarains.material.view.planets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_planets.*
import geekbarains.material.R
import geekbarains.material.util.ZoomOutPageTransformer

private const val EARTH = 0
private const val MARS = 1

class PlanetsFragment : Fragment() {

    companion object {
        fun newInstance() = PlanetsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_planets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.adapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        tab_layout.setupWithViewPager(viewPager)
        setHighlightedTab(EARTH)

        viewPager.setPageTransformer(true, ZoomOutPageTransformer())

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                setHighlightedTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
        })
    }

    private fun setHighlightedTab(position: Int) {
        val layoutInflater = LayoutInflater.from(requireContext())

        tab_layout.getTabAt(EARTH)?.customView = null
        tab_layout.getTabAt(MARS)?.customView = null

        when (position) {
            EARTH -> {
                setEarthTabHighlighted(layoutInflater)
            }
            MARS -> {
                setMarsTabHighlighted(layoutInflater)
            }
            else -> {
                setEarthTabHighlighted(layoutInflater)
            }
        }
    }

    private fun setEarthTabHighlighted(layoutInflater: LayoutInflater) {
        val earth =
            layoutInflater.inflate(R.layout.tab_earth, null)
        earth.findViewById<AppCompatTextView>(R.id.tab_image_textview)
            .setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
        tab_layout.getTabAt(EARTH)?.customView = earth
        tab_layout.getTabAt(MARS)?.customView =
            layoutInflater.inflate(R.layout.tab_mars, null)
    }

    private fun setMarsTabHighlighted(layoutInflater: LayoutInflater) {
        val mars =
            layoutInflater.inflate(R.layout.tab_mars, null)
        mars.findViewById<AppCompatTextView>(R.id.tab_image_textview)
            .setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
        tab_layout.getTabAt(EARTH)?.customView =
            layoutInflater.inflate(R.layout.tab_earth, null)
        tab_layout.getTabAt(MARS)?.customView = mars

    }
}