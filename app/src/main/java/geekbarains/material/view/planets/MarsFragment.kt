package geekbarains.material.view.planets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import kotlinx.android.synthetic.main.fragment_mars.*
import geekbarains.material.Constant.NASA_TIME_ZONE
import geekbarains.material.Constant.SIGNAL_ARRIVAL_TIME_FROM_MARS
import geekbarains.material.R
import geekbarains.material.model.AppState
import geekbarains.material.model.rover.Rover
import geekbarains.material.util.toast
import geekbarains.material.viewmodel.planets.MarsFragmentViewModel
import java.text.SimpleDateFormat
import java.util.*

class MarsFragment : Fragment() {

    private val viewModel: MarsFragmentViewModel by lazy {
        ViewModelProvider(this).get(MarsFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_mars, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sdf = SimpleDateFormat(getString(R.string.dateFormat), Locale.US)
        val cal = Calendar.getInstance(TimeZone.getTimeZone(NASA_TIME_ZONE))

        cal.add(Calendar.DAY_OF_YEAR, SIGNAL_ARRIVAL_TIME_FROM_MARS)

        val itemDate: String = sdf.format(cal.time)

        val roversName = resources.getStringArray(R.array.rovers_name)
        val roversDateStart = resources.getStringArray(R.array.rovers_date_start)
        val roversDateEnd = resources.getStringArray(R.array.rovers_date_end)

        val rovers = mutableListOf<Rover>()
        for (i in roversName.indices) {
            val arrayName = "camera_rover_$i"
            val arrayNameID =
                resources.getIdentifier(arrayName, "array", requireActivity().packageName)
            val cameras = resources.getStringArray(arrayNameID).toList()
            rovers.add(Rover(roversName[i], roversDateStart[i], roversDateEnd[i], cameras))
        }

        viewModel.getData(rovers.last().name, itemDate, rovers.last().cameras.first())
            .observe(
                viewLifecycleOwner, {
                    renderData(it)
                }
            )
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.SuccessMars -> {
                val serverResponseData = data.serverResponseData
                if (serverResponseData.photos.isNotEmpty()) {
                    imageViewMars.load(serverResponseData.photos.first().img_src)
                }
            }
            is AppState.Loading -> {
            }
            is AppState.Error -> {
                toast(data.error.message)
            }
        }
    }
}