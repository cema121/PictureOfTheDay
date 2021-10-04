package geekbarains.material.model

import geekbarains.material.model.retrofit.APODServerResponseData
import geekbarains.material.model.retrofit.epic.EPICServerResponseData
import geekbarains.material.model.retrofit.mars.MarsServerResponseData

sealed class AppState {
    data class Success(val serverResponseData: APODServerResponseData) : AppState()
    data class SuccessEPIC(val serverResponseData: EPICServerResponseData) : AppState()
    data class SuccessMars(val serverResponseData: MarsServerResponseData) : AppState()
    data class Error(val error: Throwable) : geekbarains.material.model.AppState()
    data class Loading(val progress: Int?) : geekbarains.material.model.AppState()
}