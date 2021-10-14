package geekbarains.material.view.picture

import geekbarains.material.model.retrofit.response.APODServerResponseData

sealed class PictureOfTheDayData {
    data class Success(val serverResponseData: APODServerResponseData) : PictureOfTheDayData()
    data class Error(val error: Throwable) : PictureOfTheDayData()
    data class Loading(val progress: Int?) : PictureOfTheDayData()
}
