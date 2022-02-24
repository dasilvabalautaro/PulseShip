package com.globalhiddenodds.pulseship.util

import android.graphics.Bitmap
import android.util.Log
import com.globalhiddenodds.pulseship.di.IoDispatcher
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FaceRecognition @Inject constructor(@IoDispatcher
                                          private val ioDispatcher: CoroutineDispatcher){
    private var listFaces =  listOf<Face>()
    private val tag = "FaceRecognition"

    private suspend fun recognitionFace(bmp: Bitmap){
        val image: InputImage = InputImage.fromBitmap(bmp, 0)
        val options: FaceDetectorOptions = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()
        val detector: FaceDetector = FaceDetection.getClient(options)
        detector.process(image)
            .addOnSuccessListener {
                listFaces = it.toList()

            }
            .addOnFailureListener{
                Log.e(tag, it.message!!)
            }.await()
    }

    private fun contourDetection(faces: List<Face>,
                                 bmp: Bitmap): Bitmap? = runBlocking{
        var bitMap: Bitmap? = null

        if (faces.isEmpty()){
            Log.i(tag, "Not face found")
            return@runBlocking bitMap
        }
        val job = launch {
            for (face in faces){
                val x = face.boundingBox.centerX()
                val y = face.boundingBox.centerY()
                val left = x - (face.boundingBox.width() / 2.0f)
                val top = y - (face.boundingBox.height() / 2.0f)
                val width = face.boundingBox.width()
                val height = face.boundingBox.height()

                bitMap = Bitmap.createBitmap(bmp, left.toInt(),
                    top.toInt(), width, height)
                break
            }

        }
        job.join()

        return@runBlocking bitMap
    }

    suspend fun detectFace(image: Bitmap): Bitmap?{
        return withContext(ioDispatcher){
            recognitionFace(image)
            contourDetection(listFaces, image)
        }
    }
}