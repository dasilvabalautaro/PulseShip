package com.globalhiddenodds.pulseship.ui.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import com.globalhiddenodds.pulseship.ui.fragments.camera.decodeExifOrientation
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.max

class BitmapUtil @Inject constructor() {
    private var maxSize: Int = 1024
    var orientation: Int = 1

    private val bitmapOptions = BitmapFactory.Options().apply {
        inJustDecodeBounds = false
        // Keep Bitmaps at less than 1 MP
        if (max(outHeight, outWidth) > maxSize) {
            val scaleFactorX = outWidth / maxSize + 1
            val scaleFactorY = outHeight / maxSize + 1
            inSampleSize = max(scaleFactorX, scaleFactorY)
        }
    }

    private val bitmapTransformation: Matrix by lazy { decodeExifOrientation(orientation) }

    fun decodeBitmap(buffer: ByteArray, length: Int): Bitmap {

        // Load bitmap from given buffer
        val bitmap = BitmapFactory.decodeByteArray(buffer,
            0, length, bitmapOptions)

        // Transform bitmap orientation using provided metadata
        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, bitmapTransformation, true)
    }

    fun decodeBitmap(inputBmp: Bitmap, typeResize: Int = 0): Bitmap {
        if (typeResize == 1) maxSize = 512
        val stream = ByteArrayOutputStream()
        inputBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val buffer = stream.toByteArray()
        // Load bitmap from given buffer
        val bitmap = BitmapFactory.decodeByteArray(buffer,
            0, buffer.size, bitmapOptions)

        // Transform bitmap orientation using provided metadata
        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, bitmapTransformation, true)

    }
}