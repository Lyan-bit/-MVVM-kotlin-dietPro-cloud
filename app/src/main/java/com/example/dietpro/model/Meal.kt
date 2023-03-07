package com.example.dietpro.model

import java.util.HashMap

class Meal {

    init {
        MealAllInstances.add(this)
    }

    companion object {
        var MealAllInstances = ArrayList<Meal>()
        fun createMeal(): Meal {
            return Meal()
        }
        
        var MealIndex: HashMap<String, Meal> = HashMap<String, Meal>()
        
        fun createByPKMeal(idx: String): Meal {
            var result: Meal? = MealIndex[idx]
            if (result != null) { return result }
                  result = Meal()
            MealIndex.put(idx,result)
                  result.mealId = idx
                  return result
        }
        
		fun killMeal(idx: String?) {
            val rem = MealIndex[idx] ?: return
            val remd = ArrayList<Meal>()
            remd.add(rem)
            MealIndex.remove(idx)
            MealAllInstances.removeAll(remd)
        }        
    }

    var mealId = ""  /* identity */
    var mealName = "" 
    var calories = 0.0 
    var dates = "" 
    var images = "" 
    var analysis = ""  /* derived */
    var userName = "" 
    var eatenBy : User? = null

}
