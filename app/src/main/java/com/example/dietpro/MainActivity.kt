package com.example.dietpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.dietpro.adapter.PagerAdapter
import com.example.dietpro.fragments.ListMealFragment
import com.example.dietpro.fragments.ListUserFragment
import com.example.dietpro.viewmodel.MealCRUDViewModel
import com.example.dietpro.viewmodel.UserCRUDViewModel
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() , ListMealFragment.OnListMealFragmentInteractionListener, ListUserFragment.OnListUserFragmentInteractionListener {

  private lateinit var mealvm: MealCRUDViewModel
    private lateinit var uservm: UserCRUDViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myPagerAdapter = PagerAdapter(this, supportFragmentManager)
        val viewpager: ViewPager = findViewById(R.id.view_pager)
        viewpager.adapter = myPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewpager)
        mealvm = MealCRUDViewModel(this)
        uservm = UserCRUDViewModel(this)
    }

    override fun onListMealFragmentInteraction(item: MealVO) {
        mealvm.setSelectedMeal(item)
    }

    override fun onListUserFragmentInteraction(item: UserVO) {
        uservm.setSelectedUser(item)
    }
}
