package me.pasindu.mathgame

import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.os.Handler
import android.widget.ImageButton
import android.widget.TextView
import kotlin.random.Random
import kotlin.random.nextInt

class GameLogicActivity : AppCompatActivity(), View.OnClickListener {

    private var expressionOne: TextView? = null
    private var expressionTwo: TextView? = null
    private var result: TextView? = null
    private var btnGreater: Button? = null
    private var btnEquals: Button? = null
    private var btnLess: Button? = null
    private var timerView: TextView? = null
    private var timerBar: ProgressBar? = null
    private var btnMute: ImageButton? = null
    private var tickPlayer: MediaPlayer? = null
    private var handler: Handler? = null

    private var savedExpOnePair: Pair<String, Double>? = null
    private var savedExpTwoPair: Pair<String, Double>? = null
    private var expOnePair: Pair<String, Double>? = null
    private var expTwoPair: Pair<String, Double>? = null
    private var expOneVal = 0
    private var expTwoVal = 0
    private var questionCount = 0
    private var correctCount = 0
    private var tempCorrectCount = 0
    private var state = false
    private var gameTime = 50

    private val updateTimerText = object : Runnable {
        override fun run() {
            reduceOneSecond()
            handler!!.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_logic)

        expressionOne = findViewById(R.id.tvEx1)
        expressionTwo = findViewById(R.id.tvEx2)
        result = findViewById(R.id.tvResult)
        btnGreater = findViewById(R.id.btnGT)
        btnEquals = findViewById(R.id.btnET)
        btnLess = findViewById(R.id.btnLT)
        btnMute = findViewById(R.id.btnMute)
        timerView = findViewById(R.id.timerText)
        timerBar = findViewById(R.id.barTimer)

        val tickAssetId = resources.getIdentifier("tick", "raw", packageName)
        tickPlayer = MediaPlayer.create(this, tickAssetId)

        timerBar!!.max = 50

        btnGreater!!.setOnClickListener(this)
        btnEquals!!.setOnClickListener(this)
        btnLess!!.setOnClickListener(this)
        btnMute!!.setOnClickListener(this)

        if (savedInstanceState != null) {
            gameTime = savedInstanceState.getInt("time", 0)
            savedExpOnePair = Pair(
                savedInstanceState.getString("EXP1F", ""), savedInstanceState.getDouble(
                    "EXP1S",
                    0.0
                )
            )

            savedExpTwoPair = Pair(
                savedInstanceState.getString("EXP2F", ""), savedInstanceState.getDouble(
                    "EXP2S",
                    0.0
                )
            )
            if (savedExpOnePair!!.first != "" && savedExpTwoPair!!.first != "") {
                setExpressionToWidgets()
            }
            questionCount = savedInstanceState.getInt("qC", 0)
            correctCount = savedInstanceState.getInt("cC", 0)
            tempCorrectCount = savedInstanceState.getInt("tCC", 0)
            val stateOfBtn = savedInstanceState.getBoolean("state", false)
            state = stateOfBtn
            onMuteClick(!state)
        }

        handler = Handler(Looper.getMainLooper())

        setExpression()

    }

    /**
     * This Method [setExpression] Sets Random Math expressions to [TextView]'s
     */
    private fun setExpression() {
        do {
            expOnePair = getRandomExpression()
        } while (expOnePair!!.second < 0 || expOnePair!!.second > 100)
        do {
            expTwoPair = getRandomExpression()
        } while (expTwoPair!!.second < 0 || expTwoPair!!.second > 100)
        setExpressionToWidgets()
        Log.d("EXP1 ${expOnePair!!.first}", "$expOneVal")
        Log.d("EXP2 ${expTwoPair!!.first}", "$expTwoVal")
    }

    /**
     *
     */
    private fun setExpressionToWidgets() {
        if (savedExpOnePair != null && savedExpTwoPair != null) {
            if (savedExpOnePair!!.first != "" && savedExpTwoPair!!.first != "") {
                expOnePair = savedExpOnePair
                expTwoPair = savedExpTwoPair
            }
        }
        expressionOne!!.text = expOnePair!!.first
        expOneVal = expOnePair!!.second.toInt()
        expressionTwo!!.text = expTwoPair!!.first
        expTwoVal = expTwoPair!!.second.toInt()
    }

    /**
     * This Method [getRandomExpression] Generate Random Math expressions with random terms
     *
     * @return [Pair] of random math expression [String] and its value [Double]
     */
    private fun getRandomExpression(): Pair<String, Double> {
        val operators = listOf("/", "*", "+", "-").shuffled()
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
                    Random.nextInt(1..20 / 2) * 2
                } else {
                    Random.nextInt(1..20 / 2) * 2 + 1
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
        questionCount++
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

        object : CountDownTimer(1000, 500) {
            override fun onTick(p0: Long) {
//                Do Nothing
            }

            override fun onFinish() {
                setExpression()
            }
        }
    }

    private fun onMuteClick(btnState: Boolean) {
        state = when (btnState) {
            true -> {
                btnMute!!.setImageResource(R.drawable.ic_round_volume_up_48)
                tickPlayer!!.setVolume(1.0F, 1.0F)
                !btnState
            }

            false -> {
                btnMute!!.setImageResource(R.drawable.ic_round_volume_off_48)
                tickPlayer!!.setVolume(0.0F, 0.0F)
                !btnState
            }
        }
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
            btnMute!!.id -> {
                when (state) {
                    true -> {
                        onMuteClick(true)
                    }
                    false -> {
                        onMuteClick(false)
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("time", gameTime)
        outState.putInt("qC", questionCount)
        outState.putInt("cC", correctCount)
        outState.putInt("tCC", tempCorrectCount)
        outState.putString("EXP1F", expOnePair!!.first)
        outState.putString("EXP2F", expTwoPair!!.first)
        outState.putDouble("EXP1S", expOnePair!!.second)
        outState.putDouble("EXP2S", expTwoPair!!.second)
        outState.putBoolean("state", state)
    }

    override fun onPause() {
        super.onPause()
        handler!!.removeCallbacks(updateTimerText)
    }

    override fun onResume() {
        super.onResume()
        handler!!.post(updateTimerText)
    }

    private fun reduceOneSecond() {
        if (gameTime > 0) {
            gameTime -= 1
            timerView!!.text = gameTime.toString()
            timerBar!!.progress = gameTime
            tickPlayer!!.start()
        }
    }
}
