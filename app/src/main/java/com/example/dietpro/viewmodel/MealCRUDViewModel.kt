package com.example.dietpro.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.dietpro.database.FirebaseDbi
import com.example.dietpro.model.Meal
import com.example.dietpro.model.MealVO

class MealCRUDViewModel constructor(context: Context): ViewModel() {

    private var cdb: FirebaseDbi = FirebaseDbi.getInstance()


    companion object {
        private var instance: MealCRUDViewModel? = null
        fun getInstance(context: Context): MealCRUDViewModel {
            return instance ?: MealCRUDViewModel(context)
        }
    }

    /* This metatype code requires OclType.java, OclAttribute.java, OclOperation.java */
    fun initialiseOclTypes() {
        val mealOclType: OclType = OclType.createByPKOclType("Meal")
        mealOclType.setMetatype(Meal::class.java)
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
            val itemx = Meal.createByPKMeal(meal.getMealId())
            itemx.mealId = meal.getMealId()
            itemx.mealName = meal.getMealName()
            itemx.userName = meal.getUserName()
            itemx.dates = meal.getDates()
            itemx.calories = meal.getCalories()
            itemx.analysis = meal.getAnalysis()
            itemx.images = meal.getImages()
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
            res.add(currentMeals[x].getMealId() + "")
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
        var obj = getMealByPK(x.getMealId())
        if (obj == null) {
            obj = Meal.createByPKMeal(x.getMealId())
        }
        obj.mealId = x.getMealId()
        obj.mealName = x.getMealName()
        obj.userName = x.getUserName()
        obj.calories = x.getCalories()
        obj.analysis = x.getAnalysis()
        obj.dates = x.getDates()
        obj.images = x.getImages()
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
            if ( currentMeals[x].getMealId() == id) {
                val vo: MealVO = currentMeals[x]
                val itemx = Meal.createByPKMeal(vo.getMealId())
                itemx.mealId = vo.getMealId()
                itemx.mealName = vo.getMealName()
                itemx.calories = vo.getCalories()
                itemx.dates = vo.getDates()
                itemx.images = vo.getImages()
                itemx.analysis = vo.getAnalysis()
                itemx.userName = vo.getUserName()
                itemsList.add(itemx)
            }
        }
        return itemsList
    }

    fun allMealuserNames() : ArrayList<String> {
        val res: ArrayList<String> = ArrayList()
        for (x in currentMeals.indices) {
            res.add(currentMeals[x].getUserName())
        }
        return res
    }

    fun allMealdates() : ArrayList<String> {
        val res: ArrayList<String> = ArrayList()
        for (x in currentMeals.indices) {
            res.add(currentMeals[x].getDates())
        }
        return res
    }

    fun searchByMealdates(dates: String) : ArrayList<Meal> {
        var itemsList = ArrayList<Meal>()
        for (x in currentMeals.indices) {
            if ( currentMeals[x].getDates() == dates) {
                val vo: MealVO = currentMeals[x]
                val itemx = Meal.createByPKMeal(vo.getMealId())
                itemx.mealId = vo.getMealId()
                itemx.mealName = vo.getMealName()
                itemx.calories = vo.getCalories()
                itemx.dates = vo.getDates()
                itemx.images = vo.getImages()
                itemx.analysis = vo.getAnalysis()
                itemx.userName = vo.getUserName()
                itemsList.add(itemx)
            }
        }
        return itemsList
    }



//    fun searchMealById(search: String) : MealVO {
//        var res = MealVO()
//        for (x in currentMeals.indices) {
//            if ( currentMeals[x].getMealId().toString() == search)
//                res = currentMeals[x]
//        }
//        return res
//    }

//    fun addUsereatsMeal(userName: String, mealId: String) {
//        dbm.addUsereatsMeal(userName, mealId)
//    }
//
//    fun removeUsereatsMeal(userName: String, mealId: String) {
//        dbm.removeUsereatsMeal(userName, mealId)
//    }

//    fun stringListMeal(): ArrayList<String> {
//        currentMeals = dbm.listMeal()
//        val res: ArrayList<String> = ArrayList()
//        for (x in currentMeals.indices) {
//            res.add(currentMeals[x].toString())
//        }
//        return res
//    }
//
//    fun getMeals(): ArrayList<Meal> {
//        val res: List<MealVO> = dbm.listMeal()
//        val vo: ArrayList<Meal> = ArrayList()
//        for (meal in res) {
//            val itemx = Meal.createByPKMeal(meal.getMealId())
//            itemx.mealId = meal.getMealId()
//            itemx.mealName = meal.getMealName()
//            itemx.userName = meal.getUserName()
//            itemx.dates = meal.getDates()
//            itemx.calories = meal.getCalories()
//            itemx.analysis = meal.getAnalysis()
//            itemx.images = meal.getImages()
//            itemx
//            vo.add(itemx)
//        }
//        return vo
//    }
//
//    fun getMealByPK(value: String): Meal? {
//        val res: List<MealVO> = dbm.searchByMealid(value)
//        return if (res.isEmpty()) {
//            null
//        } else {
//            val vo: MealVO = res[0]
//            val itemx = Meal.createByPKMeal(value)
//            itemx.mealId = vo.getMealId()
//            itemx.mealName = vo.getMealName()
//            itemx.userName = vo.getUserName()
//            itemx.calories = vo.getCalories()
//            itemx.analysis = vo.getAnalysis()
//            itemx.dates = vo.getDates()
//            itemx.images = vo.getImages()
//            itemx
//        }
//    }
//
//    fun retrieveMeal(value: String): Meal? {
//        return getMealByPK(value)
//    }
//
//    fun allMealids(): ArrayList<String> {
//        currentMeals = dbm.listMeal()
//        val res: ArrayList<String> = ArrayList()
//        for (meal in currentMeals.indices) {
//            res.add(currentMeals[meal].getMealId())
//        }
//        return res
//    }
//
//    fun allMealnames(): ArrayList<String> {
//        currentMeals = dbm.listMeal()
//        val res: ArrayList<String> = ArrayList()
//        for (meal in currentMeals.indices) {
//            res.add(currentMeals[meal].getMealName())
//        }
//        return res
//    }
//
//    fun allMealdates(): ArrayList<String> {
//        currentMeals = dbm.listMeal()
//        val res: ArrayList<String> = ArrayList()
//        for (meal in currentMeals.indices) {
//            res.add(currentMeals[meal].getDates())
//        }
//        return res
//    }
//
//    fun allMealuserNames(): ArrayList<String> {
//        currentMeals = dbm.listMeal()
//        val res: ArrayList<String> = ArrayList()
//        for (meal in currentMeals.indices) {
//            res.add(currentMeals[meal].getUserName())
//        }
//        return res
//    }
//
//    fun allMealcaloriess(): ArrayList<String> {
//        currentMeals = dbm.listMeal()
//        val res: ArrayList<String> = ArrayList()
//        for (meal in currentMeals.indices) {
//            res.add(currentMeals[meal].getCalories().toString())
//        }
//        return res
//    }
//
//    fun allMealanalysiss(): ArrayList<String> {
//        currentMeals = dbm.listMeal()
//        val res: ArrayList<String> = ArrayList()
//        for (meal in currentMeals.indices) {
//            res.add(currentMeals[meal].getAnalysis())
//        }
//        return res
//    }
//
//    fun allMealimages(): ArrayList<String> {
//        currentMeals = dbm.listMeal()
//        val res: ArrayList<String> = ArrayList()
//        for (meal in currentMeals.indices) {
//            res.add(currentMeals[meal].getImages())
//        }
//        return res
//    }
//
//    fun setSelectedMeal(x: MealVO) {
//        currentMeal = x
//    }
//
//    fun setSelectedMeal(i: Int) {
//        if (i < currentMeals.size) {
//            currentMeal = currentMeals[i]
//        }
//    }
//
//    fun getSelectedMeal(): MealVO? {
//        return currentMeal
//    }
//
//    fun persistMeal(x: Meal) {
//        val vo = MealVO(x)
//        dbm.editMeal(vo)
//        currentMeal = vo
//    }
//
//    fun listMeal(): ArrayList<MealVO> {
//        currentMeals = dbm.listMeal()
//        return currentMeals
//    }
//
//    fun editMeal(x: MealVO) {
//        dbm.editMeal(x)
//        currentMeal = x
//    }
//
//    fun createMeal(x: MealVO) {
//        dbm.createMeal(x)
//        currentMeal = x
//    }
//
//    fun deleteMeal(id: String) {
//        dbm.deleteMeal(id)
//        currentMeal = null
//    }
//
//    fun searchByMealid(idx: String): List<MealVO> {
//        currentMeals = dbm.searchByMealid(idx)
//        return currentMeals
//    }
//
//    fun searchByMealname(namex: String): List<MealVO> {
//        currentMeals = dbm.searchByMealname(namex)
//        return currentMeals
//    }
//
//    fun searchByMealdates(datesx: String): List<MealVO> {
//        currentMeals = dbm.searchByMealdates(datesx)
//        return currentMeals
//    }
//
//    fun searchByMealcalories(caloriesx: String): List<MealVO> {
//        currentMeals = dbm.searchByMealcalories(caloriesx)
//        return currentMeals
//    }
//
//    fun searchByMealanalysis(analysisx: String): List<MealVO> {
//        currentMeals = dbm.searchByMealanalysis(analysisx)
//        return currentMeals
//    }
//
//    fun searchByMealimages(imagesx: String): List<MealVO> {
//        currentMeals = dbm.searchByMealimages(imagesx)
//        return currentMeals
//    }
//
//    fun searchByMealuserName(userNamex: String): List<MealVO> {
//        currentMeals = dbm.searchByMealuserName(userNamex)
//        return currentMeals
//    }
//


}