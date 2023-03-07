package com.example.dietpro.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Base64
import com.example.dietpro.mlmodel.ImageClassifier
import java.io.File
import java.io.IOException

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

    fun rotateIfRequired(bitmap: Bitmap, photoFile: File) {
        try {
            val exifInterface = ExifInterface(
                photoFile.absolutePath
            )
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                rotateImage(bitmap, 90f)
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                rotateImage(bitmap, 180f)
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                rotateImage(bitmap, 270f)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Rotate the given bitmap.
     */
    private fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }
}