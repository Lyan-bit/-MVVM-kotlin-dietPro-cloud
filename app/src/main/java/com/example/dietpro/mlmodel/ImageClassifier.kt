package com.example.dietpro.mlmodel

import android.content.Context 
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import kotlin.Comparator

class ImageClassifier (myContext: Context) {
    private val assetManager: AssetManager = myContext.assets
    private val mModelPath = "converted_model.tflite" //model file
    private val mLabelPath = "labels.txt"
    private var interpreter: Interpreter
    private var labelList: List<String>
    private val INPUT_SIZE: Int = 224
    private val PIXEL_SIZE: Int = 3
    private val IMAGE_MEAN = 0
    private val IMAGE_STD = 255.0f
    private val MAX_RESULTS = 3
    private val THRESHOLD = 0.4f

    data class Recognition(
        var id: String = "",
        var title: String = "",
        var confidence: Float = 0F
    )  {
        override fun toString(): String {
            return "Title = $title, Confidence = $confidence)"
        }
    }

    init {
        val options = Interpreter.Options()
        options.setNumThreads(5)
        options.setUseNNAPI(true)
        interpreter = Interpreter(loadModelFile(assetManager), options)
        labelList = loadLabelList(assetManager)
    }

    private fun loadModelFile(assetManager: AssetManager): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(mModelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun loadLabelList(assetManager: AssetManager): List<String> {
        return assetManager.open(mLabelPath).bufferedReader().useLines { it.toList() }

    }

    /**
     * Returns the result after running the recognition with the help of interpreter
     * on the passed bitmap
     */
    fun recognizeImage(bitmap: Bitmap): List<Recognition> {
        // create a new 224 × 224 bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)
        //a structure to contain the output tensor
        val result = Array(1) { FloatArray(labelList.size) }
        interpreter.run(byteBuffer, result)
        return getSortedResult(result)
    }


    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        //To store image in a ByteArray:
        // 224 × 224 × 3 float values between 0 and 1 to represent the image
        // and 4 bytes make a float.
        val byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * PIXEL_SIZE)
        byteBuffer.order(ByteOrder.nativeOrder())
        //turn bitmap into an array of 224 × 224 integers
        // and copy the pixels in.
        val intValues = IntArray(INPUT_SIZE * INPUT_SIZE)

        //iterate through the array, reading the pixels one by one and converting them into normalized floats.
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until INPUT_SIZE) {
            for (j in 0 until INPUT_SIZE) {
                val input = intValues[pixel++]

                byteBuffer.putFloat((((input.shr(16)  and 0xFF) - IMAGE_MEAN) / IMAGE_STD))
                byteBuffer.putFloat((((input.shr(8) and 0xFF) - IMAGE_MEAN) / IMAGE_STD))
                byteBuffer.putFloat((((input and 0xFF) - IMAGE_MEAN) / IMAGE_STD))
            }
        }
        return byteBuffer
    }

    private fun getSortedResult(labelProbArray: Array<FloatArray>): List<Recognition> {
        val pq = PriorityQueue(
            MAX_RESULTS,
            Comparator<Recognition> {
                    (_, _, confidence1), (_, _, confidence2)
                -> java.lang.Float.compare(confidence1, confidence2) * -1
            })

        for (i in labelList.indices) {
            val confidence = labelProbArray[0][i]
            if (confidence >= THRESHOLD) {
                pq.add(
                    Recognition("" + i,
                    if (labelList.size > i) labelList[i] else "Unknown", confidence)
                )
            }
        }
        val recognitions = ArrayList<Recognition>()
        val recognitionsSize = Math.min(pq.size, MAX_RESULTS)

        if (pq.isNotEmpty()) {
            for (i in 0 until recognitionsSize) {
                recognitions.add(pq.poll())
            }
        }
        else {
            recognitions.add(Recognition("0", "Unknown",100F))
        }
        return recognitions
    }

}
