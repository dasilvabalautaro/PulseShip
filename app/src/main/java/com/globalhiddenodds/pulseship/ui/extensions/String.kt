package com.globalhiddenodds.pulseship.ui.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

fun String.toBitmap(): Bitmap {
    val decodedByte = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
}