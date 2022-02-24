package com.globalhiddenodds.pulseship.ui.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.globalhiddenodds.pulseship.ui.platform.BaseFragment
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.max

class ManagerFile @Inject constructor(@ApplicationContext val appContext: Context) {
    private val maxSize: Int = 1024
    private val filePhoto = "photo"

    private val bitmapOptions = BitmapFactory.Options().apply {
        inJustDecodeBounds = false
        // Keep Bitmaps at less than 1 MP
        if (max(outHeight, outWidth) > maxSize) {
            val scaleFactorX = outWidth / maxSize + 1
            val scaleFactorY = outHeight / maxSize + 1
            inSampleSize = max(scaleFactorX, scaleFactorY)
        }
    }

    private fun createFile(context: Context,
                           fileName: String,
                           extension: String): File {
        try {
            if(File(context.filesDir, "TEMP_${fileName}.$extension").exists()){
                File(context.filesDir, "TEMP_${fileName}.$extension").delete()
            }
            return File(context.filesDir, "TEMP_${fileName}.$extension")
        } catch (ex: SecurityException){
            throw SecurityException()
        }
    }

    suspend fun saveBitmap(context: Context,
                   bmp: Bitmap,
                           fileName: String): File = suspendCoroutine { cont ->
        try {
            val output = createFile(context, fileName, "jpg")
            val bytesOutput = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytesOutput)
            val byteBitmap = bytesOutput.toByteArray()

            @Suppress("BlockingMethodInNonBlockingContext")
            FileOutputStream(output).use { it.write(byteBitmap) }
            cont.resume(output)

        } catch (ex: SecurityException){
            cont.resumeWithException(ex)
        }
    }

    /** Utility function used to read input file into a byte array */
    fun loadInputBuffer(filePath: String): ByteArray {
        val inputFile = File(filePath)
        return BufferedInputStream(inputFile.inputStream()).let { stream ->
            ByteArray(stream.available()).also {
                stream.read(it)
                stream.close()
            }
        }
    }

    fun decodeBitmap(buffer: ByteArray, length: Int): Bitmap {

        // Load bitmap from given buffer
        return BitmapFactory.decodeByteArray(buffer, 0, length, bitmapOptions)

    }

    suspend fun saveFaceImage(bmp: Bitmap?){
        if (bmp != null){
            val scaleImage = Bitmap.createScaledBitmap(bmp,
                bmp.width / 2,
                bmp.height / 2, true)
            val output = saveBitmap(
                appContext,
                scaleImage, filePhoto)
            BaseFragment.pathFilePhoto = output.absolutePath

        }
    }
}