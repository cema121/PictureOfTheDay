package geekbarains.material.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import coil.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import geekbarains.material.R
import geekbarains.material.view.picture.BottomNavigationDrawerFragment
import geekbarains.material.view.picture.PictureOfTheDayData
import geekbarains.material.view.settings.SettingsFragment
import geekbarains.material.viewmodel.PictureOfTheDayViewModel
import kotlinx.android.synthetic.main.main_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    private lateinit var mainFragmentView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))

        viewModel.getData(null).observe(viewLifecycleOwner) { renderData(it) }

        mainFragmentView = view

        dayChipGroup.setOnCheckedChangeListener { dayChipGroup, position ->
            dayChipGroup.findViewById<Chip>(position)?.let {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val cal = Calendar.getInstance()

                when (position) {
                    1 -> cal.add(Calendar.DAY_OF_YEAR, -2)
                    2 -> cal.add(Calendar.DAY_OF_YEAR, -1)
                    3 -> cal.add(Calendar.DAY_OF_YEAR, 0)
                }
                val itemDate: String? = sdf.format(cal.time)

                viewModel.getData(itemDate)
                    .observe(viewLifecycleOwner) { renderData(it) }
            }
        }

        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${input_edit_text.text.toString()}")
            })
        }
        setBottomAppBar(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> toast("Favourite")
            R.id.app_bar_settings -> activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, SettingsFragment())?.addToBackStack(null)?.commit()
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url: String? = if (serverResponseData.mediaType == "image") {
                    serverResponseData.url
                } else {
                    serverResponseData.thumbs
                }
                val bottomSheetContainer = mainFragmentView.findViewById(R.id.bottom_sheet_container) as View
                if (url.isNullOrEmpty()) {
                    bottomSheetContainer.visibility = View.GONE
                    toast("Link is empty")
                } else {
                    image_view.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    bottomSheetContainer.visibility = View.VISIBLE

                    val header = bottomSheetContainer.findViewById<TextView>(R.id.bottom_sheet_description_header)
                    header.text = serverResponseData.title

                    val body = bottomSheetContainer.findViewById<TextView>(R.id.bottom_sheet_description)
                    body.text = serverResponseData.explanation
                }
            }
            is PictureOfTheDayData.Loading -> {
                //showLoading()
            }
            is PictureOfTheDayData.Error -> {
                toast(data.error.message)
            }
        }
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
        fab.setOnClickListener {
            if (isMain) {
                isMain = false
                bottom_app_bar.navigationIcon = null
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
                bottom_app_bar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                bottom_app_bar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                bottom_app_bar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }
}
