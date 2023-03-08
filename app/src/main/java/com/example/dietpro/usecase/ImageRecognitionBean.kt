package com.example.dietpro.usecase

import android.content.Context
import java.util.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Base64
import android.util.Log
import com.example.dietpro.model.Meal
import com.example.dietpro.viewmodel.ImageRecognitionViewModel
import com.example.dietpro.viewmodel.MealCRUDViewModel

class ImageRecognitionBean(c: Context) {
    private var model: MealCRUDViewModel = MealCRUDViewModel.getInstance(c)
    private var recognition: ImageRecognitionViewModel = ImageRecognitionViewModel.getInstance(c)

    private var meal = ""
	private var instanceMeal: Meal? = null
	
     private var images = ""    
     private var dimages: Bitmap? = null

    private var errors = ArrayList<String>()

    fun setMeal(mealx: String) {
        meal = mealx
    }

    fun resetData() {
        meal = ""
    }

    fun isImageRecognitionError(): Boolean {
        errors.clear()
        Log.i("meal", meal)
        instanceMeal = model.getMealByPK(meal)
        if (instanceMeal == null) {
            errors.add("meal must be a valid Meal id")
        }
        instanceMeal?.let { Log.i("img", it.analysis) }
        instanceMeal?.let { Log.i("name", it.mealName) }
        instanceMeal?.let { Log.i("calories", it.calories.toString()) }
        instanceMeal?.let { Log.i("userName", it.userName.toString()) }
        instanceMeal?.let { Log.i("dates", it.dates.toString()) }

             if (instanceMeal!!.images != "") {
		            val x = instanceMeal!!.images
		            dimages= try {
				                // convert base64 to bitmap android
				                val decodedString: ByteArray = Base64.decode(x, Base64.DEFAULT)
				                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
				                decodedByte
				            }
				            catch (e: Exception) {
				                e.message
				                null
				            }
				        } else {
				            errors.add("This is not a type of image")
				        }

        return errors.size > 0
    }

    fun errors(): String {
        return errors.toString()
    }

     fun imageRecognition (): String {
         val result = recognition.imageRecognition(dimages!!)
         instanceMeal?.analysis = result
         model.persistMeal(instanceMeal!!)
         return result
     }
}
