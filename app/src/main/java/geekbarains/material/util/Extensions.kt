package geekbarains.material.util

import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.Fragment
import geekbarains.material.Constant

fun Fragment.toast(string: String?) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.BOTTOM,
            Constant.TOAST_GRAVITY_OFFSET_X,
            Constant.TOAST_GRAVITY_OFFSET_Y)
        show()
    }
}