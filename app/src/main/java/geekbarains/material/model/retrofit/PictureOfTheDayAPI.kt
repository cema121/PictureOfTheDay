package geekbarains.material.model.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureOfTheDayAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String,
        @Query("thumbs") thumbs: Boolean = true
    ): Call<APODServerResponseData>

    @GET("planetary/apod")
    fun getPictureOfItemDay(
        @Query("api_key") apiKey: String,
        @Query("date") itemDate: String?,
        @Query("thumbs") thumbs: Boolean = true
    ): Call<APODServerResponseData>
}
