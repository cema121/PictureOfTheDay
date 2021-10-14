package geekbarains.material.model.retrofit.i

import geekbarains.material.model.retrofit.response.APODServerResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroFitAPODInterface {

    @GET("planetary/apod")
    fun getPictureOfItemDay(
        @Query("api_key") apiKey: String,
        @Query("date") itemDate: String?,
        @Query("thumbs") thumbs: Boolean = true
    ): Call<APODServerResponseData>
}
