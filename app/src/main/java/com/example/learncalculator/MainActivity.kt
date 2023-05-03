package com.example.learncalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.learncalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun onEqualClick(view: View) {

        onEqual()
        binding.intputTv.text = binding.resultTv.text.toString().drop(1)
    }

    fun onDigitClick(view: View) {

        if (stateError) {
            binding.intputTv.text = (view as Button).text
            stateError = false
        } else {
            binding.intputTv.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()

    }

    fun onAllclearClick(view: View) {
        binding.intputTv.text = ""
        binding.resultTv.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        binding.resultTv.visibility = View.GONE
    }

    fun onOperatorClick(view: View) {
        if (!stateError && lastNumeric) {

            binding.intputTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }

    fun onBackClick(view: View) {

        binding.intputTv.text = binding.intputTv.text.toString().dropLast(1)

        try {

            val lastChar = binding.intputTv.text.toString().last()

            if (lastChar.isDigit()) {
                onEqual()
            }
        } catch (e: Exception) {

            binding.resultTv.text = ""
            binding.resultTv.visibility = View.GONE
            Log.e("last char error", e.toString())
        }
    }

    fun onClearClick(view: View) {

        val builder = AlertDialog.Builder(this)

        builder.setMessage("Do you want to exit ?")

        builder.setTitle("Alert !")

        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { dialog, which ->
            finish()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }


    fun onEqual() {

        if (lastNumeric && !stateError) {

            val txt = binding.intputTv.text.toString()
            expression = ExpressionBuilder(txt).build()

            try {
                val result = expression.evaluate()
                binding.resultTv.visibility = View.VISIBLE
                binding.resultTv.text = "=" + result.toString()

            } catch (ex: ArithmeticException) {

                Log.e("evaluate error", ex.toString())
                binding.resultTv.text = "Error"
                stateError = true
                lastNumeric = true
            }
        }
    }
}


