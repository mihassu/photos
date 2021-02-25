package ru.mihassu.photos.ui.custom

import android.graphics.Bitmap
import com.squareup.picasso.Transformation
import kotlin.math.min

class FitWidthTransformation(private val targetWidth: Int) : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val ratio = source.width.toDouble() / source.height
        var scaledHeight = targetWidth / ratio
        var result = Bitmap.createScaledBitmap(source, targetWidth, scaledHeight.toInt(), false)
        if (ratio < 0.8f) {
            val trimmedHeight = targetWidth / 0.8
            val y = scaledHeight / 2 - trimmedHeight / 2
            result = Bitmap.createBitmap(result, 0, y.toInt(), targetWidth, trimmedHeight.toInt())
        }
        if (result != source) { source.recycle() }
        return result
    }

    override fun key(): String {
        return "fitWidth()"
    }
}