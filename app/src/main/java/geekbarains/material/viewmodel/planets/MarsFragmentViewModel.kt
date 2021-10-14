package geekbarains.material.viewmodel.planets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbarains.material.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import geekbarains.material.BuildConfig
import geekbarains.material.R
import geekbarains.material.model.AppState
import geekbarains.material.model.repository.NasaAPIImpl
import geekbarains.material.model.retrofit.response.MarsServerResponseData


class MarsFragmentViewModel(
    private val liveDataForViewToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitImpl: NasaAPIImpl = NasaAPIImpl(),
) : ViewModel() {

    fun getData(rover: String, earthDate: String, camera: String): LiveData<AppState> {
        sendServerRequest(rover, earthDate, camera)
        return liveDataForViewToObserve
    }

    private val callBack = object :
        Callback<MarsServerResponseData> {
        override fun onResponse(
            call: Call<MarsServerResponseData>,
            response: Response<MarsServerResponseData>,
        ) {
            if (response.isSuccessful && response.body() != null) {
                response.body()?.let {
                    liveDataForViewToObserve.value = AppState.SuccessMars(it)
                }
            } else {
                val message = response.message()
                if (message.isNullOrEmpty()) {

                    val gv: App? = null
                    if (gv != null) {
                        gv.getApplication()
                        liveDataForViewToObserve.value =
                            AppState.Error(Throwable(gv.getApplication()
                                ?.getString(R.string.undefError)))
                    }
                } else {
                    liveDataForViewToObserve.value =
                        AppState.Error(Throwable(message))
                }
            }
        }

        override fun onFailure(call: Call<MarsServerResponseData>, t: Throwable) {
            liveDataForViewToObserve.value = AppState.Error(t)
        }
    }

    private fun sendServerRequest(rover: String, earthDate: String, camera: String) {
        liveDataForViewToObserve.value = AppState.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            val gv: App? = null
            if (gv != null) {
                gv.getApplication()
                liveDataForViewToObserve.value =
                    AppState.Error(Throwable(gv.getApplication()
                        ?.getString(R.string.blankAPIKey)))
            }
        } else {
            retrofitImpl.getPhotoFromMars(rover, earthDate, camera, callBack)
        }
    }
}