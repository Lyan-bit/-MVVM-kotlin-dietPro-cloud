package com.example.dietpro.database

import com.example.dietpro.model.Meal
import com.example.dietpro.viewmodel.Ocl
import org.json.JSONObject
import java.lang.Exception
import org.json.JSONArray
import kotlin.collections.ArrayList

class DietProDAO {

    companion object {

        fun getURL(command: String?, pars: ArrayList<String>, values: ArrayList<String>): String {
            var res = "base url for the data source"
            if (command != null) {
                res += command
            }
            if (pars.isEmpty()) {
                return res
            }
            res = "$res?"
            for (item in pars.indices) {
                val par = pars[item]
                val vals = values[item]
                res = "$res$par=$vals"
                if (item < pars.size - 1) {
                    res = "$res&"
                }
            }
            return res
        }

        fun isCached(id: String?): Boolean {
            Meal.MealIndex[id] ?: return false
            return true
        }

        fun getCachedInstance(id: String): Meal? {
            return Meal.MealIndex[id]
        }

    fun parseCSV(line: String?): Meal? {
        if (line == null) {
            return null
        }
        val line1vals: ArrayList<String> = Ocl.tokeniseCSV(line)
        var mealx: Meal? = Meal.MealIndex[line1vals[0]]
        if (mealx == null) {
            mealx = Meal.createByPKMeal(line1vals[0])
        }
        mealx.mealId = line1vals[0]
        mealx.mealName = line1vals[1]
        mealx.calories = line1vals[2].toDouble()
        mealx.dates = line1vals[3]
        mealx.images = line1vals[4]
        mealx.analysis = line1vals[5]
        mealx.userName = line1vals[6]
        return mealx
    }


        fun parseJSON(obj: JSONObject?): Meal? {
            return if (obj == null) {
                null
            } else try {
                val id = obj.getString("id")
                var mealx: Meal? = Meal.MealIndex[id]
                if (mealx == null) {
                    mealx = Meal.createByPKMeal(id)
                }
                mealx.mealId = obj.getString("mealId")
                mealx.mealName = obj.getString("mealName")
                mealx.calories = obj.getDouble("calories")
                mealx.dates = obj.getString("dates")
                mealx.images = obj.getString("images")
                mealx.analysis = obj.getString("analysis")
                mealx.userName = obj.getString("userName")

                mealx
            } catch (e: Exception) {
                null
            }
        }

    fun makeFromCSV(lines: String?): ArrayList<Meal> {
        val result: ArrayList<Meal> = ArrayList<Meal>()
        if (lines == null) {
            return result
        }
        val rows: ArrayList<String> = Ocl.parseCSVtable(lines)
        for (item in rows.indices) {
            val row = rows[item]
            if (row == null || row.trim { it <= ' ' }.isEmpty()) {
                //trim
            } else {
                val x: Meal? = parseCSV(row)
                if (x != null) {
                    result.add(x)
                }
            }
        }
        return result
    }


        fun parseJSONArray(jarray: JSONArray?): ArrayList<Meal>? {
            if (jarray == null) {
                return null
            }
            val res: ArrayList<Meal> = ArrayList<Meal>()
            val len = jarray.length()
            for (i in 0 until len) {
                try {
                    val x = jarray.getJSONObject(i)
                    if (x != null) {
                        val y: Meal? = parseJSON(x)
                        if (y != null) {
                            res.add(y)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return res
        }


        fun writeJSON(x: Meal): JSONObject? {
            val result = JSONObject()
            try {
                result.put("mealId", x.mealId)
                result.put("mealName", x.mealName)
                result.put("calories", x.calories)
                result.put("dates", x.dates)
                result.put("images", x.images)
                result.put("analysis", x.analysis)
                result.put("userName", x.userName)
            } catch (e: Exception) {
                return null
            }
            return result
        }


        fun parseRaw(obj: Any?): Meal? {
             if (obj == null) {
                 return null
            }
            try {
                val map = obj as HashMap<String, Object>
                val id: String = map["id"].toString()
                var mealx: Meal? = Meal.MealIndex[id]
                if (mealx == null) {
                    mealx = Meal.createByPKMeal(id)
                }
                mealx.mealId = map["mealId"].toString()
                mealx.mealName =  map["mealName"].toString()
                mealx.calories = (map["calories"]as Long?)!!.toDouble()
                mealx.dates = map["dates"].toString()
                mealx.images = map["images"].toString()
                mealx.analysis = map["analysis"].toString()
                mealx.userName = map["userName"].toString()
                return mealx
            } catch (e: Exception) {
                return null
            }
        }

        fun writeJSONArray(es: ArrayList<Meal>): JSONArray {
            val result = JSONArray()
            for (i in 0 until es.size) {
                val ex: Meal = es[i]
                val jx = writeJSON(ex)
                if (jx == null) {
                    //null
                } else {
                    try {
                        result.put(jx)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            return result
        }
    }
}
