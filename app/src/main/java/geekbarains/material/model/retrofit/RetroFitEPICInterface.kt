package geekbarains.material.model.retrofit

import geekbarains.material.model.retrofit.epic.EPICServerResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetroFitEPICInterface {

    @GET("EPIC/api/natural/date/{date}")
    fun getEPIC(
        @Path("date") queryType1: String,
        @Query("api_key") apiKey: String,
    ): Call<EPICServerResponseData>
}
