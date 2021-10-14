package geekbarains.material.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MyView : View {
    private val mPaint = Paint()

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init()
    }

    constructor(context: Context?, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init()
    }

    // Способ рисования
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val fontSize = 100F
        val fontPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        fontPaint.textSize = fontSize
        fontPaint.style = Paint.Style.STROKE
        canvas.drawText("NasaAPI", (width/2).toFloat() - 200F, (height/2).toFloat() + 20F, fontPaint)
    }

    // Инициализация настроек кисти
    fun init() {
        // Сглаживание
        mPaint.isAntiAlias = true
        // Ширина кисти
        mPaint.strokeWidth = 20f
        // Рисуем цвет
        mPaint.color = Color.BLUE
        // Тип кисти
        mPaint.style = Paint.Style.STROKE
    }
}