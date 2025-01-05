package com.example.calculadora

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var display: EditText
    private var lastNumeric: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializamos la pantalla de la calculadora
        display = findViewById(R.id.display)

        // Configurar botones numéricos y de operaciones
        setNumericButtonListeners()
        setOperatorButtonListeners()
    }

    private fun setNumericButtonListeners() {
        val numericButtons = listOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2,
            R.id.btn_3, R.id.btn_4, R.id.btn_5,
            R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9
        )

        for (id in numericButtons) {
            findViewById<Button>(id).setOnClickListener { v ->
                display.append((v as Button).text)
                lastNumeric = true
            }
        }
    }

    private fun setOperatorButtonListeners() {
        val operatorButtons = listOf(
            R.id.btn_plus, R.id.btn_minus,
            R.id.btn_multiply, R.id.btn_divide
        )

        for (id in operatorButtons) {
            findViewById<Button>(id).setOnClickListener { v ->
                if (lastNumeric && !isOperatorAdded(display.text.toString())) {
                    display.append((v as Button).text)
                    lastNumeric = false
                    lastDot = false
                }
            }
        }

        // Botón Clear
        findViewById<Button>(R.id.btn_clear).setOnClickListener {
            display.text.clear()
            lastNumeric = false
            lastDot = false
        }

        // Botón Igual
        findViewById<Button>(R.id.btn_equals).setOnClickListener {
            if (lastNumeric) {
                calculateResult()
            }
        }
    }

    private fun calculateResult() {
        try {
            val input = display.text.toString()
            val result = evaluateExpression(input)
            display.setText(result)
        } catch (e: Exception) {
            display.setText("Error")
        }
    }

    private fun evaluateExpression(expression: String): String {
        val tokens = expression.split("(?=[+\\-*/])|(?<=[+\\-*/])".toRegex())
        var result = tokens[0].toDouble()

        var operator = ""
        for (token in tokens.drop(1)) {
            when {
                "+-*/".contains(token) -> operator = token
                operator.isNotEmpty() -> {
                    val number = token.toDouble()
                    result = when (operator) {
                        "+" -> result + number
                        "-" -> result - number
                        "*" -> result * number
                        "/" -> result / number
                        else -> result
                    }
                    operator = ""
                }
            }
        }
        return result.toString()
    }

    private fun isOperatorAdded(value: String): Boolean {
        return value.contains("+") || value.contains("-") ||
                value.contains("*") || value.contains("/")
    }
}

