package com.example.dietpro.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.dietpro.fragments.*

class PagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        private val TABTITLES = arrayOf("+Meal", "ListMeal", "EditMeal", "DeleteMeal", "SearchMealByDatedates", "+ProfileUser", "ListUser", "FindTotalConsumedCaloriesByDate", "FindTargetCalories", "FindBMR", "CaloriesProgress", "AddUsereatsMeal", "RemoveUsereatsMeal", "ImageRecognition")
    }

    override fun getItem(position: Int): Fragment {
        // instantiate a fragment for the page.
        return when (position) {
            0 -> {
                CreateMealFragment.newInstance(mContext)
            }
            1 -> {
                ListMealFragment.newInstance(mContext)
            }
            2 -> {
                EditMealFragment.newInstance(mContext)
            }
            3 -> {
                DeleteMealFragment.newInstance(mContext)
            }
            4 -> {
                SearchMealByDatedatesFragment.newInstance(mContext)
            }
            5 -> {
                CreateProfileUserFragment.newInstance(mContext)
            }
            6 -> {
                ListUserFragment.newInstance(mContext)
            }
            7 -> {
                FindTotalConsumedCaloriesByDateFragment.newInstance(mContext)
            }
            8 -> {
                FindTargetCaloriesFragment.newInstance(mContext)
            }
            9 -> {
                FindBMRFragment.newInstance(mContext)
            }
            10 -> {
                CaloriesProgressFragment.newInstance(mContext)
            }
            11 -> {
                AddUsereatsMealFragment.newInstance(mContext)
            }
            12 -> {
                RemoveUsereatsMealFragment.newInstance(mContext)
            }
            13 -> {
                ImageRecognitionFragment.newInstance(mContext)
            }
            else -> CreateProfileUserFragment.newInstance(mContext)
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return TABTITLES[position]
    }

    override fun getCount(): Int {
        return 14
    }
}
