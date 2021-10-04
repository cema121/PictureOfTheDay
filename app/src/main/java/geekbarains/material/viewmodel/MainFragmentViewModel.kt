package geekbarains.material.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbarains.material.App
import geekbarains.material.BuildConfig
import geekbarains.material.R
import geekbarains.material.model.AppState
import geekbarains.material.model.repository.NasaAPI
import geekbarains.material.model.repository.NasaAPIImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import geekbarains.material.model.retrofit.APODServerResponseData

class MainFragmentViewModel(
    private val liveDataForViewToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitImpl: NasaAPIImpl = NasaAPIImpl(),
) :
    ViewModel() {

    fun getData(itemDate: String?): LiveData<AppState> {
        sendServerRequest(itemDate)
        return liveDataForViewToObserve
    }

    private val callBack = object :
        Callback<APODServerResponseData> {
        override fun onResponse(
            call: Call<APODServerResponseData>,
            response: Response<APODServerResponseData>,
        ) {
            if (response.isSuccessful && response.body() != null) {
                liveDataForViewToObserve.value =
                    AppState.Success(response.body()!!)
            } else {
                val message = response.message()
                if (message.isNullOrEmpty()) {

                    val gv: App? = null
                    if (gv != null) {
                        gv.getApplication()
                        liveDataForViewToObserve.value =
                            AppState.Error(
                                Throwable(
                                    gv.getApplication()
                                        ?.getString(R.string.undefError)
                                )
                            )
                    }
                } else {
                    liveDataForViewToObserve.value =
                        AppState.Error(Throwable(message))
                }
            }
        }

        override fun onFailure(call: Call<APODServerResponseData>, t: Throwable) {
            liveDataForViewToObserve.value = AppState.Error(t)
        }
    }

    private fun sendServerRequest(itemDate: String?) {
        liveDataForViewToObserve.value = AppState.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            val gv: App? = null
            if (gv != null) {
                gv.getApplication()
                liveDataForViewToObserve.value =
                    AppState.Error(
                        Throwable(
                            gv.getApplication()
                                ?.getString(R.string.blankAPIKey)
                        )
                    )
            }
        } else {
            retrofitImpl.getPictureOfDayRetroFit(itemDate, callBack)
        }
    }
}