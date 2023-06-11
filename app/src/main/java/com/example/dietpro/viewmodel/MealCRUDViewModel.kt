package com.example.dietpro.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.dietpro.MealVO
import com.example.dietpro.database.FirebaseDbi
import com.example.dietpro.model.Meal

class MealCRUDViewModel constructor(context: Context): ViewModel() {

    private var cdb: FirebaseDbi = FirebaseDbi.getInstance()


    companion object {
        private var instance: MealCRUDViewModel? = null
        fun getInstance(context: Context): MealCRUDViewModel {
            return instance ?: MealCRUDViewModel(context)
        }
    }

    private var currentMeal: MealVO? = null
    private var currentMeals: ArrayList<MealVO> = ArrayList()

    fun listMeal(): ArrayList<MealVO> {
        val meals: ArrayList<Meal> = Meal.MealAllInstances
        currentMeals.clear()
        for (i in meals.indices) {
            currentMeals.add(MealVO(meals[i]))
        }
        return currentMeals
    }

        fun getMeals(): ArrayList<Meal> {
        val res: List<MealVO> = listMeal()
        val vo: ArrayList<Meal> = ArrayList()
        for (meal in res) {
            val itemx = Meal.createByPKMeal(meal.mealId)
            itemx.mealId = meal.mealId
            itemx.mealName = meal.mealName
            itemx.userName = meal.userName
            itemx.dates = meal.dates
            itemx.calories = meal.calories
            itemx.analysis = meal.analysis
            itemx.images = meal.images
            itemx
            vo.add(itemx)
        }
        return vo
    }

    fun stringListMeal(): ArrayList<String> {
        val res: ArrayList<String> = ArrayList()
        for (x in currentMeals.indices) {
            res.add(currentMeals[x].toString() + "")
        }
        return res
    }

    fun getMealByPK(value: String): Meal? {
        return Meal.MealIndex[value]
    }

    fun retrieveMeal(value: String): Meal? {
        return getMealByPK(value)
    }

    fun allMealids(): ArrayList<String> {
        val res: ArrayList<String> = ArrayList()
        for (x in currentMeals.indices) {
            res.add(currentMeals[x].mealId + "")
        }
        return res
    }

    fun setSelectedMeal(x: MealVO) {
        currentMeal = x
    }

    fun setSelectedMeal(i: Int) {
        if (i < currentMeals.size) {
            currentMeal = currentMeals[i]
        }
    }

    fun getSelectedMeal(): MealVO? {
        return currentMeal
    }

    fun persistMeal(x: Meal) {
        val vo = MealVO(x)
        cdb.persistMeal(x)
        currentMeal = vo
    }

    fun editMeal(x: MealVO) {
        var obj = getMealByPK(x.mealId)
        if (obj == null) {
            obj = Meal.createByPKMeal(x.mealId)
        }
        obj.mealId = x.mealId
        obj.mealName = x.mealName
        obj.userName = x.userName
        obj.calories = x.calories
        obj.analysis = x.analysis
        obj.dates = x.dates
        obj.images = x.images
        cdb.persistMeal(obj)
        currentMeal = x
    }

    fun createMeal(x: MealVO) {
        editMeal(x)
    }

    fun deleteMeal(id: String) {
        val obj = getMealByPK(id)
        if (obj != null) {
            cdb.deleteMeal(obj)
            Meal.killMeal(id)
        }
        currentMeal = null
    }

    fun addUsereatsMeal(userName: String, mealId: String) {
        var obj = getMealByPK(mealId)
        if (obj == null) {
            obj = Meal.createByPKMeal(mealId)
        }
        obj.userName = userName
        cdb.persistMeal(obj)
        currentMeal = MealVO(obj)

    }

    fun removeUsereatsMeal(mealId: String) {
        var obj = getMealByPK(mealId)
        if (obj == null) {
            obj = Meal.createByPKMeal(mealId)
        }
        obj.userName = "Null"
        cdb.persistMeal(obj)
        currentMeal = MealVO(obj)

    }

    fun searchByMealid(id: String) : ArrayList<Meal> {
        var itemsList = ArrayList<Meal>()
        for (x in currentMeals.indices) {
            if ( currentMeals[x].mealId == id) {
                val vo: MealVO = currentMeals[x]
                val itemx = Meal.createByPKMeal(vo.mealId)
                itemx.mealId = vo.mealId
                itemx.mealName = vo.mealName
                itemx.calories = vo.calories
                itemx.dates = vo.dates
                itemx.images = vo.images
                itemx.analysis = vo.analysis
                itemx.userName = vo.userName
                itemsList.add(itemx)
            }
        }
        return itemsList
    }

    fun allMealuserNames() : ArrayList<String> {
        val res: ArrayList<String> = ArrayList()
        for (x in currentMeals.indices) {
            res.add(currentMeals[x].userName)
        }
        return res
    }

    fun allMealdates() : ArrayList<String> {
        val res: ArrayList<String> = ArrayList()
        for (x in currentMeals.indices) {
            res.add(currentMeals[x].dates)
        }
        return res
    }

    fun searchByMealdates(dates: String) : ArrayList<Meal> {
        var itemsList = ArrayList<Meal>()
        for (x in currentMeals.indices) {
            if ( currentMeals[x].dates == dates) {
                val vo: MealVO = currentMeals[x]
                val itemx = Meal.createByPKMeal(vo.mealId)
                itemx.mealId = vo.mealId
                itemx.mealName = vo.mealName
                itemx.calories = vo.calories
                itemx.dates = vo.dates
                itemx.images = vo.images
                itemx.analysis = vo.analysis
                itemx.userName = vo.userName
                itemsList.add(itemx)
            }
        }
        return itemsList
    }
}
