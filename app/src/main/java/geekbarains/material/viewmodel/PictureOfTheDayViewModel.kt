package geekbarains.material.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbarains.material.BuildConfig
import geekbarains.material.model.repository.PODRetrofitImpl
import geekbarains.material.model.retrofit.PODServerResponseData
import geekbarains.material.view.picture.PictureOfTheDayData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) :
    ViewModel() {

    fun getData(itemDate: String?): LiveData<PictureOfTheDayData> {
        sendServerRequest(itemDate)
        return liveDataForViewToObserve
    }

    private fun sendServerRequest(itemDate: String?) {
        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            if (itemDate == null) {
                retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey).enqueue(object :
                    Callback<PODServerResponseData> {
                    override fun onResponse(
                        call: Call<PODServerResponseData>,
                        response: Response<PODServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataForViewToObserve.value =
                                    PictureOfTheDayData.Error(Throwable("Unidentified error"))
                            } else {
                                liveDataForViewToObserve.value =
                                    PictureOfTheDayData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                        liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                    }
                })
            } else {
                retrofitImpl.getRetrofitImpl().getPictureOfItemDay(apiKey, itemDate)
                    .enqueue(object :
                        Callback<PODServerResponseData> {
                        override fun onResponse(
                            call: Call<PODServerResponseData>,
                            response: Response<PODServerResponseData>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                liveDataForViewToObserve.value =
                                    PictureOfTheDayData.Success(response.body()!!)
                            } else {
                                val message = response.message()
                                if (message.isNullOrEmpty()) {
                                    liveDataForViewToObserve.value =
                                        PictureOfTheDayData.Error(Throwable("Unidentified error"))
                                } else {
                                    liveDataForViewToObserve.value =
                                        PictureOfTheDayData.Error(Throwable(message))
                                }
                            }
                        }

                        override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                            liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                        }
                    })
            }
        }
    }
}