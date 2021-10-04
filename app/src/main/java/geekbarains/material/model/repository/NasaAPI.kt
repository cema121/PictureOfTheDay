package geekbarains.material.model.repository

import geekbarains.material.model.retrofit.APODServerResponseData
import geekbarains.material.model.retrofit.epic.EPICServerResponseData
import geekbarains.material.model.retrofit.mars.MarsServerResponseData
import retrofit2.Callback

interface NasaAPI {

    fun getPictureOfDayRetroFit(
        itemDate: String?,
        callback: Callback<APODServerResponseData>,
    )

    fun getEPIC(
        itemDate: String,
        callback: Callback<EPICServerResponseData>,
    )

    fun getPhotoFromMars(
        rover: String,
        earthDate: String,
        camera: String,
        callback: Callback<MarsServerResponseData>,
    )

}