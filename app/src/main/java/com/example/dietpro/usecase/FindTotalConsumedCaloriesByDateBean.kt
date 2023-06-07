package com.example.dietpro.usecase

import android.content.Context
import com.example.dietpro.model.Meal
import com.example.dietpro.model.User
import com.example.dietpro.viewmodel.MealCRUDViewModel
import com.example.dietpro.viewmodel.UserCRUDViewModel
import java.util.*

class FindTotalConsumedCaloriesByDateBean(c: Context) {
    private var model: MealCRUDViewModel = MealCRUDViewModel.getInstance(c)
    private var modelUser: UserCRUDViewModel = UserCRUDViewModel.getInstance(c)

    private var meals = ""
	private var instanceMeal: ArrayList<Meal>? = null
	
    private var user = ""
	private var instanceUser: User? = null
	
	private var dates = ""
	private var ddates = ""

    private var errors = ArrayList<String>()

    fun setMeals(mealsx: String) {
        meals = mealsx
    }
    
    fun setUser(userx: String) {
        user = userx
    }
    
    fun setDates(datesx: String) {
        dates = datesx
    }
    

    fun resetData() {
        meals = ""
        user = ""
        dates = ""
    }

    fun isFindTotalConsumedCaloriesByDateError(): Boolean {
        errors.clear()
        instanceMeal = model.getMeals()
        if (instanceMeal == null) {
            errors.add("meals must be a valid Meal id")
        }
        
        instanceUser = modelUser.getUserByPK(user)
        if (instanceUser == null) {
            errors.add("user must be a valid User id")
        }
        
          if (dates != "") { ddates = dates }
else {
 	  errors.add("dates cannot be empty")
 	  }
        return errors.isNotEmpty()
    }

    fun errors(): String {
        return errors.toString()
    }

     fun findTotalConsumedCaloriesByDate (): Double {
        return modelUser.findTotalConsumedCaloriesByDate(instanceMeal!!, instanceUser!!, ddates)
     }
}
