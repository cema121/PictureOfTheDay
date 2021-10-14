package geekbarains.material.model.repository

import com.google.gson.GsonBuilder
import geekbarains.material.BuildConfig
import geekbarains.material.Constant.BASE_API_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import geekbarains.material.model.retrofit.response.APODServerResponseData
import geekbarains.material.model.retrofit.i.RetroFitAPODInterface
import geekbarains.material.model.retrofit.i.RetroFitEPICInterface
import geekbarains.material.model.retrofit.i.RetroFitMarsInterface
import geekbarains.material.model.retrofit.response.EPICServerResponseData
import geekbarains.material.model.retrofit.response.MarsServerResponseData
import java.io.IOException

class NasaAPIImpl : NasaAPI {

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }

    inner class PODInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }

    private val retroFitBuilderAPOD = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(createOkHttpClient(PODInterceptor()))
        .build().create(RetroFitAPODInterface::class.java)

    private val retroFitBuilderEPIC = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(createOkHttpClient(PODInterceptor()))
        .build().create(RetroFitEPICInterface::class.java)

    private val retroFitBuilderMars = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(createOkHttpClient(PODInterceptor()))
        .build().create(RetroFitMarsInterface::class.java)


    override fun getPictureOfDayRetroFit(
        itemDate: String?,
        callback: Callback<APODServerResponseData>,
    ) {
        retroFitBuilderAPOD.getPictureOfItemDay(
            BuildConfig.NASA_API_KEY,
            itemDate
        )
            .enqueue(callback)
    }

    override fun getEPIC(
        itemDate: String,
        callback: Callback<EPICServerResponseData>
    ) {
        retroFitBuilderEPIC.getEPIC(
            itemDate,
            BuildConfig.NASA_API_KEY
        )
            .enqueue(callback)
    }

    override fun getPhotoFromMars(
        rover: String,
        earthDate: String,
        camera: String,
        callback: Callback<MarsServerResponseData>,
    ) {
        retroFitBuilderMars.getMarsPhoto(
            rover,
            earthDate,
            camera,
            BuildConfig.NASA_API_KEY
        )
            .enqueue(callback)
    }

}