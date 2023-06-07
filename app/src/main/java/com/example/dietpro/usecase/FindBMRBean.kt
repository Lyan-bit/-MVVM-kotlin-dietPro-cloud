package com.example.dietpro.usecase

import android.content.Context
import com.example.dietpro.model.User
import com.example.dietpro.viewmodel.UserCRUDViewModel
import java.util.*

class FindBMRBean(_c: Context) {
    private var model: UserCRUDViewModel = UserCRUDViewModel.getInstance(_c)

    private var user = ""
	private var instanceUser: User? = null
	

    private var errors = ArrayList<String>()

    fun setUser(userx: String) {
        user = userx
    }

    fun resetData() {
        user = ""
    }

    fun isFindBMRError(): Boolean {
        errors.clear()
        instanceUser = model.getUserByPK(user)
        if (instanceUser == null) {
            errors.add("user must be a valid User id")
        }
        return errors.isNotEmpty()
    }

    fun errors(): String {
        return errors.toString()
    }

     fun findBMR (): Double {
        return model.findBMR(instanceUser!!)
     }
}
