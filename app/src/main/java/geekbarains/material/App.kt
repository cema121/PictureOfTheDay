package geekbarains.material

import android.app.Application

class App : Application() {

    private var appInstance: Application? = null

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    fun getApplication(): Application? {
        return appInstance
    }
}
