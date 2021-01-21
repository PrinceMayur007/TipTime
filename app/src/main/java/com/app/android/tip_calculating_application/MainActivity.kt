package com.app.android.tip_calculating_application

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.app.android.tip_calculating_application.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateBtn.setOnClickListener { calculateTip() }

        binding.costOfServiceEditText.setOnKeyListener { v, keyCode, _ ->
            handleKeyEvent(
                v,
                keyCode
            )
        }
    }

    //we need to add bindingFeature to gradle before using.
    private fun calculateTip() {
        //getting the text from editTextField and then converting Editable string to String
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        //Changing to double for calculation
        val cost = stringInTextField.toDoubleOrNull()
        //to prevent putting empty string
        //we given elvis operator here for null if cost is null it will return, means not implement further ?: return
        //but we removed it add other functionality
        if (cost == null || cost == 0.0) {
            displayTip(0.0)
            return
        }

        //get tip percentage
        //checkedRadioButtonId is a variable from RadioButtonGroup which returns id of Checked RadioButton

        val tipPercentage = when (binding.radioGroup2.checkedRadioButtonId) {
            R.id.tip_20 -> 0.20
            R.id.tip_18 -> 0.18
            else -> 0.15
        }

        //calculate tip
        var tip = tipPercentage * cost
        //var caz we will be needing to roundup if user choose it, so that value might change and we need to store it in same.

        //to check user need round up
        //isChecked is attribute of Switch to check if it is on/off.
        val roundup = binding.roundUpTip.isChecked
        if (roundup) {
            tip = kotlin.math.ceil(tip)
        }

        displayTip(tip)

        //now if use doesn't enter cost of service, app will crash
        //we fixed this with toDoubleOrNull() instead of toDouble()
    }

    private fun displayTip(tip: Double) {
        //formatting tip as currency
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        //changed format string with %s in string.xml at tip_amount string
        //then we reapply the formatted text
        binding.tipAmount.text = getString(R.string.tip_amount, formattedTip)
        //we change the android:text into tools:text = "Tip Amount: $10", good and it will be not displayed you run your app, displayed after calculated.
        //as placeholder
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

}
