package com.example.dietpro.fragments
	
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dietpro.model.MealVO
import com.example.dietpro.R
import com.example.dietpro.adapter.MealRecyclerViewAdapter
import com.example.dietpro.viewmodel.MealCRUDViewModel
import java.lang.RuntimeException

class ListMealFragment : Fragment() { 
	private var mColumnCount = 1
	private var mListener: OnListMealFragmentInteractionListener? = null

	private var root: View? = null
	private lateinit var myContext: Context
	private lateinit var model: MealCRUDViewModel
		  		
    companion object {
    	const val ARG_COLUMN_COUNT = "column-count"
        fun newInstance(c: Context): ListMealFragment {
            val fragment = ListMealFragment()
            val args = Bundle()
			args.putInt(ARG_COLUMN_COUNT, 1)
			fragment.arguments = args
			fragment.myContext = c
			return fragment
		}
	}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        	mColumnCount = requireArguments().getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		val view = inflater.inflate(R.layout.listmeal_layout, container, false)
		model = MealCRUDViewModel.getInstance(myContext)

		if (view is RecyclerView) {
			val context = view.getContext()
			if (mColumnCount <= 1) {
				view.layoutManager = LinearLayoutManager(context)
			} else {
				view.layoutManager = GridLayoutManager(context, mColumnCount)
			}
		}
		root = view

		return root as View
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is OnListMealFragmentInteractionListener) {
			mListener = context
		} else {
			throw RuntimeException("$context must implement OnListMealFragmentInteractionListener")
		}
	}

	override fun onResume() {
		super.onResume()
		(root as RecyclerView).adapter = MealRecyclerViewAdapter(model.listMeal(), mListener!!)
	}

	override fun onDetach() {
		super.onDetach()
		mListener = null
	}

	interface OnListMealFragmentInteractionListener {
		fun onListMealFragmentInteraction(item: MealVO)
	}
}
