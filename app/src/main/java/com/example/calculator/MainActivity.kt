package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var displayTextView: TextView
    private var currentInput: String = ""
    private var operator: String? = null
    private var firstOperand: Double? = null
    private var secondOperand: Double? = null
    private var lastOperation: Boolean = false
    private var selectedOperatorButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayTextView = findViewById(R.id.tv_display)

        setNumberButtonListeners()
        setOperatorButtonListeners()
        setSpecialButtonListeners()
    }

    private fun setNumberButtonListeners() {
        val numberButtons = listOf(
            findViewById<Button>(R.id.btn_0),
            findViewById<Button>(R.id.btn_1),
            findViewById<Button>(R.id.btn_2),
            findViewById<Button>(R.id.btn_3),
            findViewById<Button>(R.id.btn_4),
            findViewById<Button>(R.id.btn_5),
            findViewById<Button>(R.id.btn_6),
            findViewById<Button>(R.id.btn_7),
            findViewById<Button>(R.id.btn_8),
            findViewById<Button>(R.id.btn_9)
        )

        for (button in numberButtons) {
            button.setOnClickListener {
                if (lastOperation) {
                    currentInput = ""
                    lastOperation = false
                }
                currentInput += (button as Button).text
                displayTextView.text = currentInput
            }
        }
    }

    private fun setOperatorButtonListeners() {
        val operators = mapOf(
            R.id.btn_plus to "+",
            R.id.btn_minus to "-",
            R.id.btn_multiply to "*",
            R.id.btn_divide to "/"
        )

        for ((id, symbol) in operators) {
            findViewById<Button>(id).setOnClickListener { button ->
                if (currentInput.isNotEmpty()) {
                    if (firstOperand == null) {
                        firstOperand = currentInput.toDouble()
                    }
                    operator = symbol

                    // Highlight selected operator but don't show it in the TextView
                    highlightSelectedOperator(button as Button)

                    currentInput = "" // Clear input for the next number
                }
            }
        }

        findViewById<Button>(R.id.btn_equals).setOnClickListener {
            if (operator != null && currentInput.isNotEmpty()) {
                secondOperand = currentInput.toDouble()
                val result = performOperation(firstOperand, secondOperand, operator!!)
                displayTextView.text = result.toString()
                firstOperand = result
                operator = null
                currentInput = result.toString()
                lastOperation = true

                // Clear operator highlight after calculation
                clearOperatorHighlight()
            }
        }
    }


    private fun setSpecialButtonListeners() {
        findViewById<Button>(R.id.btn_c).setOnClickListener {
            currentInput = ""
            firstOperand = null
            secondOperand = null
            operator = null
            displayTextView.text = "0"
            clearOperatorHighlight()
        }

        findViewById<Button>(R.id.btn_ce).setOnClickListener {
            currentInput = ""
            displayTextView.text = "0"
        }

        findViewById<Button>(R.id.btn_bs).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = currentInput.dropLast(1)
                displayTextView.text = if (currentInput.isEmpty()) "0" else currentInput
            }
        }

        findViewById<Button>(R.id.btn_sign).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = if (currentInput.startsWith("-")) {
                    currentInput.drop(1)
                } else {
                    "-$currentInput"
                }
                displayTextView.text = currentInput
            }
        }

        findViewById<Button>(R.id.btn_dot).setOnClickListener {
            if (!currentInput.contains(".")) {
                currentInput += "."
                displayTextView.text = currentInput
            }
        }
    }

    private fun performOperation(firstOperand: Double?, secondOperand: Double?, operator: String): Double {
        return when (operator) {
            "+" -> firstOperand!! + secondOperand!!
            "-" -> firstOperand!! - secondOperand!!
            "*" -> firstOperand!! * secondOperand!!
            "/" -> if (secondOperand != 0.0) firstOperand!! / secondOperand!! else 0.0
            else -> 0.0
        }
    }

    private fun highlightSelectedOperator(button: Button) {
        // Clear previous highlight
        clearOperatorHighlight()

        // Highlight the selected operator button
        button.setBackgroundColor(resources.getColor(R.color.teal_200)) // Change to your desired highlight color
        selectedOperatorButton = button
    }

    private fun clearOperatorHighlight() {
        selectedOperatorButton?.setBackgroundColor(resources.getColor(R.color.e8e4e4)) // Reset to default background
        selectedOperatorButton = null
    }
}
