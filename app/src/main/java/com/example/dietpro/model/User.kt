package com.example.dietpro.model

import java.util.HashMap

class User {

    init {
        UserAllInstances.add(this)
    }

    companion object {
        var UserAllInstances = ArrayList<User>()
        fun createUser(): User {
            return User()
        }
        
        var UserIndex: HashMap<String, User> = HashMap<String, User>()
        
        fun createByPKUser(idx: String): User {
            var result: User? = UserIndex[idx]
            if (result != null) { return result }
                  result = User()
                  UserIndex.put(idx,result)
                  result.userName = idx
                  return result
        }
        
		fun killUser(idx: String?) {
            val rem = UserIndex[idx] ?: return
            val remd = ArrayList<User>()
            remd.add(rem)
            UserIndex.remove(idx)
            UserAllInstances.removeAll(remd)
        }        
    }

    var userName = ""  /* identity */
    var gender = "" 
    var heights = 0.0 
    var weights = 0.0 
    var activityLevel = "" 
    var age = 0.0 
    var targetCalories = 0.0  /* derived */
    var totalConsumedCalories = 0.0  /* derived */
    var bmr = 0.0  /* derived */
	    fun calculateBmr() : Double {
		
		var result : Double
            if (gender == "male") {
                            bmr  = 66.5 + (13 * weights) + (5 * heights) - (6.76 * age)
				        } else {
                            bmr  = 66.5 + (9.56 * weights) + (1.8 * heights) - (4.68 * age)
				        }
				          result  = bmr
		return result
		             
}				    fun calculateTargetCalories() : Double {
		
		var result : Double
				        if (activityLevel == "low") {
					            targetCalories  = bmr * 1.375
				        } else {
				              if (activityLevel == "moderate") {
				      	            targetCalories  = bmr * 1.55
				              } else {
				                      targetCalories  = bmr * 1.725
				              }
				        }
				          result  = targetCalories
		return result
		             
}			
    var meals = ArrayList<Meal>()

}
