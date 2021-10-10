package geekbarains.material.view.planets

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.load
import com.bumptech.glide.Glide
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
import geekbarains.material.model.retrofit.model.epic.EPICItem
import geekbarains.material.util.toast
import geekbarains.material.viewmodel.planets.EarthFragmentViewModel
import kotlinx.android.synthetic.main.fragment_earth.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class EarthFragment : Fragment() {

    private var isExpanded = false
    private var itemImage = 0
    private var earthDayToPhoto = 0
    private var serverResponseData = arrayListOf<EPICItem>()
    private lateinit var sdf: SimpleDateFormat

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

        sdf = SimpleDateFormat(getString(R.string.dateFormat), Locale.US)
        sdf.timeZone = TimeZone.getTimeZone(NASA_TIME_ZONE)
        val cal = Calendar.getInstance(TimeZone.getTimeZone(NASA_TIME_ZONE))
        cal.add(Calendar.DAY_OF_YEAR, SIGNAL_ARRIVAL_TIME_FROM_EARTH - earthDayToPhoto)
        val itemDate: String = sdf.format(cal.time)
        dateEarthImage.text = getString(R.string.itemImageDate, itemDate)

        sendData(cal)

        buttonPrev.setOnClickListener { switchImage(-1) }
        buttonNext.setOnClickListener { switchImage(1) }

        imageViewEarth.setOnClickListener {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(
                earthContainer,
                TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            val params: ViewGroup.LayoutParams = imageViewEarth.layoutParams
            params.height =
                if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            imageViewEarth.layoutParams = params
            imageViewEarth.scaleType =
                if (isExpanded) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
        }
        setFAB()
    }

    private fun setFAB() {
        setInitialState()
        fabEarth.setOnClickListener {
            if (isExpanded) {
                collapseFab()
            } else {
                expandFAB()
            }
        }
    }

    private fun setInitialState() {
        transparent_background.apply {
            alpha = 0f
        }
        option_two_container.apply {
            alpha = 0f
            isClickable = false
        }
        option_one_container.apply {
            alpha = 0f
            isClickable = false
        }
    }

    private fun switchImage(direction: Int) {
        if (itemImage + direction < serverResponseData.size && itemImage + direction >= 0) {
            itemImage += direction
        }
        countEarthImage.text =
            getString(R.string.itemImage, (itemImage + 1), serverResponseData.size)
        if (serverResponseData.isNotEmpty())
            dateEarthImage.text =
                getString(R.string.itemImageDate, serverResponseData[itemImage].date)
        val imageUrl = serverResponseData[itemImage].image
        val imageDate = serverResponseData[itemImage].date
        val imageFullPath = createImageUrl(imageUrl, imageDate)
        imageViewEarth.load(imageFullPath)
        imageViewEarth.contentDescription = imageFullPath
    }

    private fun shareImage(
        imageFileName: String,
        context: Context,
        dateImage: String,
        planetName: String,
    ) {
        var imagePath: String?

        CoroutineScope(Dispatchers.IO).launch {
            imagePath = saveImage(
                Glide.with(context)
                    .asBitmap()
                    .load(imageFileName)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .error(android.R.drawable.stat_notify_error)
                    .submit()
                    .get(),
                dateImage,
                context,
                planetName,
                itemImage
            )

            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/*"

            imagePath?.let {
                val f = File(it)

                val imageUri = FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    f
                )

                share.putExtra(
                    Intent.EXTRA_STREAM,
                    imageUri
                )
                startActivity(Intent.createChooser(share, "Share Image"))
            }
        }
    }

    private fun expandFAB() {
        isExpanded = true
        ObjectAnimator.ofFloat(plus_imageview, "rotation", 0f, 225f).start()
        ObjectAnimator.ofFloat(option_two_container, "translationY", -130f).start()
        ObjectAnimator.ofFloat(option_one_container, "translationY", -250f).start()

        option_two_container.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    option_two_container.isClickable = true
                    option_two_container.setOnClickListener {
                        toast("Функционал не реализован")
                    }
                }
            })
        option_one_container.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    option_one_container.isClickable = true
                    option_one_container.setOnClickListener {
                        if (serverResponseData.isNotEmpty()) {
                            val cal = Calendar.getInstance(TimeZone.getTimeZone(NASA_TIME_ZONE))
                            cal.add(
                                Calendar.DAY_OF_YEAR,
                                SIGNAL_ARRIVAL_TIME_FROM_EARTH - earthDayToPhoto
                            )
                            shareImage(
                                imageViewEarth.contentDescription.toString(),
                                requireContext(),
                                sdf.format(cal.time),
                                "Earth"
                            )
                        }
                    }
                }
            })
        transparent_background.animate()
            .alpha(0.9f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    transparent_background.isClickable = true
                }
            })
    }

    private fun collapseFab() {
        isExpanded = false
        ObjectAnimator.ofFloat(plus_imageview, "rotation", 0f, -180f).start()
        ObjectAnimator.ofFloat(option_two_container, "translationY", 0f).start()
        ObjectAnimator.ofFloat(option_one_container, "translationY", 0f).start()

        option_two_container.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    option_two_container.isClickable = false
                    option_one_container.setOnClickListener(null)
                }
            })
        option_one_container.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    option_one_container.isClickable = false
                }
            })
        transparent_background.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    transparent_background.isClickable = false
                }
            })
    }

    private fun createImageUrl(imageUrl: String, imageDateStr: String): String {
        var year = EPIC_IMAGE_URL_DEFAULT_YEAR
        var month = EPIC_IMAGE_URL_DEFAULT_MONTH
        var day = EPIC_IMAGE_URL_DEFAULT_DAY
        val imageDate = SimpleDateFormat(DATE_FORMAT).parse(imageDateStr)
        imageDate?.let {
            sdf = SimpleDateFormat(getString(R.string.dateFormatYear), Locale.US)
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

    private fun sendData(curDate: Calendar) {
        val itemDate: String = sdf.format(curDate.time)
        viewModel.getData(itemDate).observe(viewLifecycleOwner) { renderData(it) }
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.SuccessEPIC -> {
                serverResponseData = data.serverResponseData

                if (serverResponseData.isNotEmpty()) {
                    countEarthImage.text =
                        getString(R.string.itemImage, (itemImage + 1), serverResponseData.size)
                    dateEarthImage.text =
                        getString(R.string.itemImageDate, serverResponseData.first().date)
                    val imageUrl = serverResponseData.first().image
                    val imageDate = serverResponseData.first().date
                    val imageFullPath = createImageUrl(imageUrl, imageDate)
                    imageViewEarth.load(imageFullPath)
                    imageViewEarth.contentDescription = imageFullPath
                } else {
                    var cal = Calendar.getInstance(TimeZone.getTimeZone(NASA_TIME_ZONE))
                    cal.add(Calendar.DAY_OF_YEAR, SIGNAL_ARRIVAL_TIME_FROM_EARTH - earthDayToPhoto)

                    earthDayToPhoto++
                    cal = Calendar.getInstance(TimeZone.getTimeZone(NASA_TIME_ZONE))
                    cal.add(Calendar.DAY_OF_YEAR, SIGNAL_ARRIVAL_TIME_FROM_EARTH - earthDayToPhoto)
                    val itemDate: String = sdf.format(cal.time)
                    dateEarthImage.text = getString(R.string.tryText, itemDate)
                    sendData(cal)
                }
            }
            is AppState.Loading -> {
            }
            is AppState.Error -> {
                toast(data.error.message)
            }
        }
    }

    private fun saveImage(
        image: Bitmap,
        dateImage: String,
        context: Context,
        planetName: String,
        itemImage: Int
    ): String? {
        var savedImagePath: String? = null
        val imageFileName = "Image_from_${planetName}_${dateImage}_$itemImage.jpg"
        val storageDir = context.cacheDir

        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            imageFile.createNewFile()
            savedImagePath = imageFile.absolutePath
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.flush()
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return savedImagePath
    }

    private fun galleryAddPic(imagePath: String?, context: Context) {
        imagePath?.let { path ->
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(path)
            val contentUri: Uri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            context.sendBroadcast(mediaScanIntent)
        }
    }
}