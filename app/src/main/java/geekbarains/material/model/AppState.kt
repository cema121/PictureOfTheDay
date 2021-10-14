package geekbarains.material.model

import geekbarains.material.model.retrofit.response.APODServerResponseData
import geekbarains.material.model.retrofit.response.EPICServerResponseData
import geekbarains.material.model.retrofit.response.MarsServerResponseData

sealed class AppState {
    data class SuccessAPOD(val serverResponseData: APODServerResponseData) : AppState()
    data class SuccessEPIC(val serverResponseData: EPICServerResponseData) : AppState()
    data class SuccessMars(val serverResponseData: MarsServerResponseData) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class Loading(val progress: Int?) : AppState()
}