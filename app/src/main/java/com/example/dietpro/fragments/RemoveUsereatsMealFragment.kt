package com.example.dietpro.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.dietpro.R
import com.example.dietpro.model.MealBean
import com.example.dietpro.model.UserBean
import com.example.dietpro.viewmodel.MealCRUDViewModel
import com.example.dietpro.viewmodel.UserCRUDViewModel

class RemoveUsereatsMealFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var root: View
    private lateinit var myContext: Context
    private lateinit var model: MealCRUDViewModel
    private lateinit var modelUser: UserCRUDViewModel
    
    private lateinit var userBean: UserBean
    private lateinit var mealBean: MealBean

    private lateinit var mealIdSpinner: Spinner
    private lateinit var userNameSpinner: Spinner

    private var allMealmealIds: List<String> = ArrayList()
    private var allUseruserNames: List<String> = ArrayList()

    private lateinit var mealIdTextField: EditText
    private var mealIdData = ""
    private lateinit var userNameTextField: EditText
    private var userNameData = ""
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button

    companion object {
        fun newInstance(c: Context): RemoveUsereatsMealFragment {
            val fragment =  RemoveUsereatsMealFragment()
            val args = Bundle()
            fragment.arguments = args
            fragment.myContext = c
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.removeusereatsmeal_layout, container, false)
        super.onViewCreated(root, savedInstanceState)
        return root
    }

    override fun onResume() {
        super.onResume()
        model = MealCRUDViewModel.getInstance(myContext)
        modelUser = UserCRUDViewModel.getInstance(myContext)
        mealBean = MealBean(myContext)

        Log.i("model",model.listMeal().toString())
        Log.i("model",model.allMealids().toString())
        Log.i("model",model.allMealuserNames().toString())

        allMealmealIds = model.allMealids()
        mealIdTextField =
            root.findViewById<View>(R.id.removeUsereatsMealmealIdField) as EditText
        mealIdSpinner =
            root.findViewById<View>(R.id.removeUsereatsMealmealIdSpinner) as Spinner
        val mealIdAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item, allMealmealIds)
        mealIdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mealIdSpinner.setAdapter(mealIdAdapter)
        mealIdSpinner.setOnItemSelectedListener(this)

		allUseruserNames = model.allMealuserNames()
        userNameTextField =
            root.findViewById<View>(R.id.removeUsereatsMealuserNameField) as EditText
        userNameSpinner =
            root.findViewById<View>(R.id.removeUsereatsMealuserNameSpinner) as Spinner
        val userNameAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item, allUseruserNames)
        userNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userNameSpinner.setAdapter(userNameAdapter)
        userNameSpinner.setOnItemSelectedListener(this)

        userBean = UserBean(myContext)

        okButton = root.findViewById(R.id.removeUsereatsMealOK)
        okButton.setOnClickListener(this)
        cancelButton = root.findViewById(R.id.removeUsereatsMealCancel)
        cancelButton.setOnClickListener(this)
    }

    override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int, id: Long) {
        if (parent === mealIdSpinner) {
	            mealIdTextField.setText(allMealmealIds[position])
	        }
	    if (parent ==userNameSpinner) {
	            userNameTextField.setText(allUseruserNames[position])
	        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    	//onNothingSelected
    }

    override fun onClick(v: View?) {
        val imm = myContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        try {
            imm.hideSoftInputFromWindow(v?.windowToken, 0)
        } catch (e: Exception) {
        	 e.message
        }

        when (v?.id) {
            R.id.removeUsereatsMealOK -> {
                removeUsereatsMealOK()
            }
            R.id.removeUsereatsMealCancel -> {
                removeUsereatsMealCancel()
            }
        }
    }

    private fun removeUsereatsMealOK() {
        mealIdData = mealIdTextField.getText().toString() + ""
        mealBean.setMealId(mealIdData)
        userNameData = userNameTextField.getText().toString() + ""
        mealBean.setUserName(userNameData)
        if (mealBean.isRemoveUsereatsMealError()) {
            Log.w(javaClass.name, userBean.errors())
            Toast.makeText(myContext, "Errors: " + userBean.errors(), Toast.LENGTH_LONG).show()
        } else {
            mealBean.removeUsereatsMeal()
            Toast.makeText(myContext, "removed!", Toast.LENGTH_LONG).show()
        }
    }

    private fun removeUsereatsMealCancel() {
        userBean.resetData()
        mealIdTextField.setText("")
        userNameTextField.setText("")
    }
}
