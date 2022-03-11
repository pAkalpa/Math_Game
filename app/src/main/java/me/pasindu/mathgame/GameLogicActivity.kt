package me.pasindu.mathgame

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random
import kotlin.random.nextInt

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
        var expOnePair: Pair<String, Double>
        var expTwoPair: Pair<String, Double>
        do {
            expOnePair = getRandomExpression()
        } while (expOnePair.second < 0 || expOnePair.second > 100)
        do {
            expTwoPair = getRandomExpression()
        } while (expTwoPair.second < 0 || expTwoPair.second > 100)
        expressionOne!!.text = expOnePair.first
        expOneVal = expOnePair.second.toInt()
        expressionTwo!!.text = expTwoPair.first
        expTwoVal = expTwoPair.second.toInt()
        Log.d("EXP1 ${expOnePair.first}", "$expOneVal")
        Log.d("EXP2 ${expTwoPair.first}", "$expTwoVal")
        questionCount++
    }

    /**
     * This Method [getRandomExpression] Generate Random Math expressions with random terms
     *
     * @return [Pair] of random math expression [String] and its value [Double]
     */
    private fun getRandomExpression(): Pair<String, Double> {
        val operators = listOf("/", "*", "+", "-")
        val firstTerm = (1..20).random()
        var expVal: Double = firstTerm.toDouble()
        val noOfTerms = (2..3).random()
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
            var nextTerm = (1..20).random()
            if (operator == "/" && i == 1) {
                nextTerm = (1..firstTerm).random()
            } else if (operator == "/" && i >= 2 && expVal.toInt() > 0) {
                nextTerm = (1..expVal.toInt()).random()
            }
            if (i > 2) {
                nextTerm = if (expVal % 2 == 0.toDouble()) {
                    Random.nextInt(1..20 / 2) * 2;
                } else {
                    Random.nextInt(1..20 / 2) * 2 + 1;
                }
            }

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