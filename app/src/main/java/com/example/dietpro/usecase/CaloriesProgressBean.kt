package com.example.dietpro.usecase

import android.content.Context
import com.example.dietpro.model.User
import com.example.dietpro.viewmodel.UserCRUDViewModel
import java.util.*

class CaloriesProgressBean(c: Context) {
    private var model: UserCRUDViewModel = UserCRUDViewModel.getInstance(c)

    private var user = ""
	private var instanceUser: User? = null
	

    private var errors = ArrayList<String>()

    fun setUser(userx: String) {
        user = userx
    }

    fun resetData() {
        user = ""
    }

    fun isCaloriesProgressError(): Boolean {
        errors.clear()
        instanceUser = model.getUserByPK(user)
        if (instanceUser == null) {
            errors.add("user must be a valid User id")
        }
        
        return errors.size > 0
    }

    fun errors(): String {
        return errors.toString()
    }

     fun caloriesProgress (): Double {
        return model.caloriesProgress(instanceUser!!)
     }
}
