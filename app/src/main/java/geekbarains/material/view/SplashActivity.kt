package geekbarains.material.view

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import geekbarains.material.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private var handler = Handler()
    private lateinit var rocketAnimation: AnimationDrawable

    override fun onStart() {
        super.onStart()
        rocketAnimation.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val splashScreen = image_view.apply {
            setBackgroundResource(R.drawable.animation_splash_screen)
            rocketAnimation = background as AnimationDrawable
        }
        splashScreen.setOnClickListener({ rocketAnimation.start() })

        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

}