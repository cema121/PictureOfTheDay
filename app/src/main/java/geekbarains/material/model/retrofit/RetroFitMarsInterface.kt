package geekbarains.material.model.retrofit

import geekbarains.material.model.retrofit.mars.MarsServerResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetroFitMarsInterface {

    @GET("mars-photos/api/v1/rovers/{rover}/photos")
    fun getMarsPhoto(
        @Path("rover") rover: String,
        @Query("earth_date") earthDate: String,
        @Query("camera") camera: String,
        @Query("api_key") apiKey: String,
    ): Call<MarsServerResponseData>
}
