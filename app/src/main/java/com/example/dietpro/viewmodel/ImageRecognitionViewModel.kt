package com.example.dietpro.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Base64
import com.example.dietpro.mlmodel.ImageClassifier
import java.io.File

class ImageRecognitionViewModel constructor(myContext: Context) {

    private var imageClassifier: ImageClassifier = ImageClassifier(myContext)

    companion object {
        private var instance: ImageRecognitionViewModel? = null
        fun getInstance(context: Context): ImageRecognitionViewModel {
            return instance ?: ImageRecognitionViewModel(context)
        }
    }

    fun imageRecognition (img: Bitmap): String {
        val result = imageClassifier.recognizeImage(img)
        return result[0].title
    }

    /**
     * getCapturedImage():
     * Decodes and crops the captured image from camera.
     */
    fun getCapturedImage(photoFile: File, width: Int, height: Int): Bitmap {
        // Get the dimensions of the View
        val targetW = width
        val targetH = height

        var bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true

        BitmapFactory.decodeFile(photoFile.absolutePath)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight
        val scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH))

        bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inMutable = true
        return BitmapFactory.decodeFile(photoFile.absolutePath, bmOptions)
    }
}
