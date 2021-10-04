package geekbarains.material.view.planets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import kotlinx.android.synthetic.main.fragment_earth.*
import geekbarains.material.BuildConfig
import geekbarains.material.Constant.DATE_FORMAT
import geekbarains.material.Constant.EPIC_IMAGE_URL
import geekbarains.material.Constant.EPIC_IMAGE_URL_DEFAULT_DAY
import geekbarains.material.Constant.EPIC_IMAGE_URL_DEFAULT_MONTH
import geekbarains.material.Constant.EPIC_IMAGE_URL_DEFAULT_YEAR
import geekbarains.material.Constant.EPIC_IMAGE_URL_DEL
import geekbarains.material.Constant.EPIC_IMAGE_URL_OTHERS
import geekbarains.material.Constant.EPIC_IMAGE_URL_PNG
import geekbarains.material.Constant.NASA_TIME_ZONE
import geekbarains.material.Constant.SIGNAL_ARRIVAL_TIME_FROM_EARTH
import geekbarains.material.R
import geekbarains.material.model.AppState
import geekbarains.material.util.toast
import geekbarains.material.viewmodel.planets.EarthFragmentViewModel
import java.text.SimpleDateFormat
import java.util.*

class EarthFragment : Fragment() {

    private val viewModel: EarthFragmentViewModel by lazy {
        ViewModelProvider(this).get(EarthFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_earth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sdf = SimpleDateFormat(getString(R.string.dateFormat), Locale.US)
        val cal = Calendar.getInstance(TimeZone.getTimeZone(NASA_TIME_ZONE))

        cal.add(Calendar.DAY_OF_YEAR, SIGNAL_ARRIVAL_TIME_FROM_EARTH)

        val itemDate: String = sdf.format(cal.time)

        viewModel.getData(itemDate).observe(viewLifecycleOwner, { renderData(it) })
    }

    private fun createImageUrl(imageUrl: String, imageDateStr: String): String {
        var year = EPIC_IMAGE_URL_DEFAULT_YEAR
        var month = EPIC_IMAGE_URL_DEFAULT_MONTH
        var day = EPIC_IMAGE_URL_DEFAULT_DAY
        val imageDate = SimpleDateFormat(DATE_FORMAT).parse(imageDateStr)
        imageDate?.let {
            var sdf = SimpleDateFormat(getString(R.string.dateFormatYear), Locale.US)
            year = sdf.format(it.time)

            sdf = SimpleDateFormat(getString(R.string.dateFormatMonth), Locale.US)
            month = sdf.format(it.time)

            sdf = SimpleDateFormat(getString(R.string.dateFormatDay), Locale.US)
            day = sdf.format(it.time)
        }

        return EPIC_IMAGE_URL + year +
                EPIC_IMAGE_URL_DEL + month +
                EPIC_IMAGE_URL_DEL + day +
                EPIC_IMAGE_URL_DEL + EPIC_IMAGE_URL_PNG +
                EPIC_IMAGE_URL_DEL + imageUrl +
                EPIC_IMAGE_URL_OTHERS + BuildConfig.NASA_API_KEY
    }


    private fun renderData(data: AppState) {
        when (data) {
            is AppState.SuccessEPIC -> {
                val serverResponseData = data.serverResponseData

                if (serverResponseData.isNotEmpty()) {
                    val imageUrl = serverResponseData.first().image
                    val imageDate = serverResponseData.first().date
                    val imageFullPath = createImageUrl(imageUrl, imageDate)
                    imageViewEarth.load(imageFullPath)
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