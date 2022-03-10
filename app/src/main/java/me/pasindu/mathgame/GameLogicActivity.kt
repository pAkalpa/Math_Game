package me.pasindu.mathgame

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class GameLogicActivity : AppCompatActivity(), View.OnClickListener{

    private var expressionOne: TextView? = null
    private var expressionTwo: TextView? = null
    private var result: TextView? = null
    private var btnGreater: Button? = null
    private var btnEquals: Button? = null
    private var btnLess: Button? = null

    private var expOneVal = 0
    private var expTwoVal = 0
    private var questionCount = 0
    private var correctCount = 0
    private var tempCorrectCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_logic)

        expressionOne = findViewById(R.id.tvEx1)
        expressionTwo = findViewById(R.id.tvEx2)
        result = findViewById(R.id.tvResult)
        btnGreater = findViewById(R.id.btnGT)
        btnEquals = findViewById(R.id.btnET)
        btnLess = findViewById(R.id.btnLT)

        btnGreater!!.setOnClickListener(this)
        btnEquals!!.setOnClickListener(this)
        btnLess!!.setOnClickListener(this)

    }

    /**
     * This Method [setExpression] Sets Random Math expressions to [TextView]'s
     */
    private fun setExpression() {
        val (exp1, val1) = getRandomExpression()
        val (exp2, val2) = getRandomExpression()
        expressionOne!!.text = exp1
        expOneVal = val1
        expressionTwo!!.text = exp2
        expTwoVal = val2
        Log.d("EXP1 $exp1", "$val1")
        Log.d("EXP2 $exp2", "$val2")
        questionCount++
    }

    /**
     * This Method [getRandomExpression] Generate Random Math expressions with random terms
     *
     * @return [Pair] of random math expression [String] and its value [Int]
     */
    private fun getRandomExpression(): Pair<String, Int> {
        val operators = listOf("/", "*", "+", "-")
        val firstTerm = (1..20).random()
        var expVal = firstTerm
        val noOfTerms = (2..4).random()
        val expression = mutableListOf<String>()

        if (noOfTerms >= 2) {
            for (i in 1 until noOfTerms) {
                expression.add("(")
            }
        }

        expression.add(firstTerm.toString())

        for (i in 1..noOfTerms) {
            if (i >= 2) {
                expression.add(")")
            }
            val operator = operators.random()
            expression.add(operator)
            val nextTerm = (1..20).random()
            expression.add(nextTerm.toString())

            when (operator) {
                "*" -> expVal *= nextTerm
                "/" -> expVal /= nextTerm
                "+" -> expVal += nextTerm
                "-" -> expVal -= nextTerm
            }
        }
        return Pair(expression.joinToString(separator = " "), expVal)
    }

    /**
     * This [getCorrectAnswer] Method Check for Correct Answer
     *
     * @param [operator]
     */
    private fun getCorrectAnswer(operator: String) {
        if ((expOneVal > expTwoVal) && (operator == ">")) {
            result!!.text = getString(R.string.correct)
            result!!.setTextColor(Color.parseColor("#FF99CC00"))
            correctCount++
            tempCorrectCount++
        } else if ((expOneVal == expTwoVal) && (operator == "=")) {
            result!!.text = getString(R.string.correct)
            result!!.setTextColor(Color.parseColor("#FF99CC00"))
            correctCount++
            tempCorrectCount++
        } else if ((expOneVal < expTwoVal) && (operator == "<")) {
            result!!.text = getString(R.string.correct)
            result!!.setTextColor(Color.parseColor("#FF99CC00"))
            correctCount++
            tempCorrectCount++
        } else {
            result!!.text = getString(R.string.wrong)
            result!!.setTextColor(Color.parseColor("#FFFF4444"))
            tempCorrectCount = 0
        }
        Log.d("EXP QCount", "$questionCount")
        Log.d("EXP CCount", "$correctCount")
        Log.d("EXP TCCount", "$tempCorrectCount")
    }

    /**
     * This Overridden Method [onClick] set click listeners for [Button]'s
     *
     * @param [view]
     */
    override fun onClick(view: View?) {
        when (view!!.id) {
            btnGreater!!.id -> getCorrectAnswer(">")
            btnEquals!!.id -> getCorrectAnswer("=")
            btnLess!!.id -> getCorrectAnswer("<")
        }
    }
}