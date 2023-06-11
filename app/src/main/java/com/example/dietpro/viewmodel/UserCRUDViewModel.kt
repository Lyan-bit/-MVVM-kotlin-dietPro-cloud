package com.example.dietpro.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.dietpro.ModelPreferencesManager
import com.example.dietpro.model.UserVO
import com.example.dietpro.model.Meal
import com.example.dietpro.model.User
import java.util.ArrayList

class UserCRUDViewModel constructor(context: Context): ViewModel() {

    private var currentUser: UserVO? = null
    private var currentUsers: ArrayList<UserVO> = ArrayList()

    init {
        ModelPreferencesManager.with(context, "User_DATA")
        currentUser = getUser()
    }

    companion object {
        private var instance: UserCRUDViewModel? = null
        fun getInstance(context: Context): UserCRUDViewModel {
            return instance ?: UserCRUDViewModel(context)
        }
    }

    fun getUser() : UserVO? {
        currentUsers.clear()
        currentUser = ModelPreferencesManager.get<User>("KEY_User")?.let { UserVO(it) }
        currentUser?.let { currentUsers.add(0, it) }
        return currentUser
    }

    fun createUser(_x: UserVO) {
        ModelPreferencesManager.put(_x, "KEY_User")
        currentUser = _x
        currentUser?.let { currentUsers.add(0, it) }
    }

    fun setSelectedUser(x: UserVO) {
        currentUser = x
    }



    fun listUser(): ArrayList<UserVO> {
        getUser()
        return currentUsers
    }

    fun listAllUser(): ArrayList<User> {
        currentUsers = listUser()
        var res = ArrayList<User>()
        for (x in currentUsers.indices) {
            val vo: UserVO = currentUsers[x]
            val itemx = User.createByPKUser(vo.userName)
            itemx.userName = vo.userName
            itemx.gender = vo.gender
            itemx.heights = vo.heights
            itemx.weights = vo.weights
            itemx.activityLevel = vo.activityLevel
            itemx.age = vo.age
            itemx.targetCalories = vo.targetCalories
            itemx.totalConsumedCalories = vo.totalConsumedCalories
            itemx.bmr = vo.bmr
            res.add(itemx)
        }
        return res
    }

    fun stringListUser(): ArrayList<String> {
        currentUsers = listUser()
        val res: ArrayList<String> = ArrayList()
        for (x in currentUsers.indices) {
            res.add(currentUsers[x].toString())
        }
        return res
    }

    fun getUserByPK(_val: String): User? {
        val res: ArrayList<UserVO> = listUser()
        return if (res.isEmpty()) {
            null
        } else {
            val vo: UserVO = res[0]
            val itemx = User.createByPKUser(_val)
            itemx.userName = vo.userName
            itemx.gender = vo.gender
            itemx.heights = vo.heights
            itemx.weights = vo.weights
            itemx.activityLevel = vo.activityLevel
            itemx.age = vo.age
            itemx.targetCalories = vo.targetCalories
            itemx.totalConsumedCalories = vo.totalConsumedCalories
            itemx.bmr = vo.bmr
            itemx
        }
    }

    fun retrieveUser(_val: String): User? {
        return getUserByPK(_val)
    }

    fun allUserUserNames(): ArrayList<String> {
        currentUsers = listUser()
        val res: ArrayList<String> = ArrayList()
        for (user in currentUsers.indices) {
            res.add(currentUsers[user].userName)
        }
        return res
    }

    fun setSelectedUser(i: Int) {
        if (i < currentUsers.size) {
            currentUser = currentUsers[i]
        }
    }

    fun getSelectedUser(): UserVO? {
        return currentUser
    }

    fun persistUser(_x: User) {
        val vo = UserVO(_x)
        createUser(vo)
        currentUser = vo
    }

    fun findTargetCalories(user: User): Double {
        var result : Double
        user.targetCalories  = user.calculateTargetCalories()
        persistUser (user)
        result  = user.targetCalories
        return result
    }

    fun findBMR(user: User): Double {
        var result : Double
        user.bmr  = user.calculateBmr()
        persistUser (user)
        result  = user.bmr
        return result
    }


    fun caloriesProgress(user: User): Double {
        var result : Double
        var progress: Double
        progress  = (user.totalConsumedCalories / user.targetCalories) * 100
        persistUser (user)
        result  = progress
        return result
    }

    fun findTotalConsumedCaloriesByDate(meals: ArrayList<Meal>, user: User, dates: String): Double {
        var totalConsumedCalories: Double = 0.0
        for (meal in meals) {
            if (meal.userName == user.userName && meal.dates == dates) {
                totalConsumedCalories += meal.calories
            }
        }
        user.totalConsumedCalories  = totalConsumedCalories
        persistUser (user)
        return totalConsumedCalories
    }
}
