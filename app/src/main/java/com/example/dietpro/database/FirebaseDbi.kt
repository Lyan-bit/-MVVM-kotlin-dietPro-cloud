package com.example.dietpro.database

import com.example.dietpro.model.Meal
import com.example.dietpro.model.MealVO
import com.google.firebase.database.*

class FirebaseDbi() {

    var database: DatabaseReference? = null

    companion object {
        private var instance: FirebaseDbi? = null
        fun getInstance(): FirebaseDbi {
            return instance ?: FirebaseDbi()
        }
    }

    init {
        connectByURL("https://dietpro-e8868-default-rtdb.europe-west1.firebasedatabase.app/")
    }

    fun connectByURL(url: String) {
        database = FirebaseDatabase.getInstance(url).reference
        if (database == null) {
            return
        }
        val mealListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get instances from the cloud database
                val meal = dataSnapshot.value as Map<String, Object>?
                if (meal != null) {
                    val keys = meal.keys
                    for (key in keys) {
                        val x = meal[key]
                        DietProDAO.parseRaw(x)
                    }
                    // Delete local objects which are not in the cloud:
                    val locals = ArrayList<Meal>()
                    locals.addAll(Meal.MealAllInstances)
                    for (x in locals) {
                        if (keys.contains(x.mealId)) {
                            //check
                        } else {
                            Meal.killMeal(x.mealId)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //cancel
            }
        }
        database!!.child("meals").addValueEventListener(mealListener)
    }

    fun persistMeal(ex: Meal) {
        val evo = MealVO(ex)
        val key = evo.getMealId()
        if (database == null) {
            return
        }
        database!!.child("meals").child(key).setValue(evo)
    }

    fun deleteMeal(ex: Meal) {
        val key: String = ex.mealId
        if (database == null) {
            return
        }
        database!!.child("meals").child(key).removeValue()
    }
}
