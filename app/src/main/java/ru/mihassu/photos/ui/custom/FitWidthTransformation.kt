package ru.mihassu.photos.ui.custom

import android.graphics.Bitmap
import com.squareup.picasso.Transformation
import kotlin.math.min

class FitWidthTransformation(val targetWidth: Int) : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val result = Bitmap.createBitmap(source, 0, 0, source.width, source.height)
        if (result != source) { source.recycle() }
        return result
    }

    override fun key(): String {
        return "fitWidth()"
    }
}