package com.globalhiddenodds.pulseship.ui.extensions

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.toBase64String(): String {
    ByteArrayOutputStream().apply {
        compress(Bitmap.CompressFormat.JPEG, 10, this)
        return Base64.encodeToString(toByteArray(), Base64.DEFAULT)
    }
}