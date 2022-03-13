package me.pasindu.mathgame

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.os.Handler
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlin.random.Random
import kotlin.random.nextInt

class GameLogicActivity : AppCompatActivity(), View.OnClickListener {

    //    expression one text view declaration
    private var expressionOne: TextView? = null

    //    expression two text view declaration
    private var expressionTwo: TextView? = null

    //    result text view Declaration
    private var result: TextView? = null

    //    greater button declaration
    private var btnGreater: Button? = null

    //    equal button declaration
    private var btnEquals: Button? = null

    //    less button declaration
    private var btnLess: Button? = null

    //    timer TextView declaration
    private var timerView: TextView? = null

    //    ProgressBar declaration
    private var timerBar: ProgressBar? = null

    //    mute button declaration
    private var btnMute: ImageButton? = null

    //    MediaPlayer declaration
    private var tickPlayer: MediaPlayer? = null

    //    handler declaration
    private var handler: Handler? = null

    //    null pair to retrieve saved data
    private var savedExpOnePair: Pair<String, Double>? = null

    //    null pair to retrieve saved data
    private var savedExpTwoPair: Pair<String, Double>? = null

    //    expression one pair
    private var expOnePair: Pair<String, Double>? = null

    //    expression two pair
    private var expTwoPair: Pair<String, Double>? = null

    //    expression one value
    private var expOneVal = 0

    //    expression two value
    private var expTwoVal = 0

    //    question count
    private var questionCount = 0

    //    correct Answer count
    private var correctCount = 0

    //    temporary correct count
    private var tempCorrectCount = 0

    //    save mute btn state
    private var state = false

    //    full game time in seconds
    private var gameTime = 50

    //    save total elapsed time in seconds`
    private var totalTimeElapsed = gameTime

    /**
     * second, K., Karapanos, A., Truong, S., Sachwani, A. and Genedy, S., 2022. Kotlin: call a function every second.
     * (online) Stack Overflow.
     * Available at: <https://stackoverflow.com/questions/55570990/kotlin-call-a-function-every-second> [Accessed 25 February 2022].
     */
//    time update object
    private val updateTimerText = object : Runnable {
        override fun run() {
            reduceOneSecond()
            handler!!.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_logic)

//        Assign id's to variables
        expressionOne = findViewById(R.id.tvEx1)
        expressionTwo = findViewById(R.id.tvEx2)
        result = findViewById(R.id.tvResult)
        btnGreater = findViewById(R.id.btnGT)
        btnEquals = findViewById(R.id.btnET)
        btnLess = findViewById(R.id.btnLT)
        btnMute = findViewById(R.id.btnMute)
        timerView = findViewById(R.id.timerText)
        timerBar = findViewById(R.id.barTimer)

//        get tick audio resource id and assign it to variable
        val tickAssetId = resources.getIdentifier("tick", "raw", packageName)
//        create new media player object
        tickPlayer = MediaPlayer.create(this, tickAssetId)

//        progress bar maximum value
        timerBar!!.max = 50

//        set click listeners to all the buttons
        btnGreater!!.setOnClickListener(this)
        btnEquals!!.setOnClickListener(this)
        btnLess!!.setOnClickListener(this)
        btnMute!!.setOnClickListener(this)

//        Retrieve data from savedInstanceState
        if (savedInstanceState != null) {
//            load current timer value from Instance State
            gameTime = savedInstanceState.getInt("time", 0)
//            load expression one pair
            savedExpOnePair = Pair(
                savedInstanceState.getString("EXP1F", ""), savedInstanceState.getDouble(
                    "EXP1S",
                    0.0
                )
            )

//            load expression two pair
            savedExpTwoPair = Pair(
                savedInstanceState.getString("EXP2F", ""), savedInstanceState.getDouble(
                    "EXP2S",
                    0.0
                )
            )

//            set expressions to text views if expression one and two expressions not empty
            if (savedExpOnePair!!.first != "" && savedExpTwoPair!!.first != "") {
                setExpressionToWidgets(false)
            }

//            load question count
            questionCount = savedInstanceState.getInt("qC", 0)
//            load correct answer count
            correctCount = savedInstanceState.getInt("cC", 0)
//            load temporary correct answer count
            tempCorrectCount = savedInstanceState.getInt("tCC", 0)
//            load total time elapsed
            totalTimeElapsed = savedInstanceState.getInt("tTE", 0)
//            load mute button state
            state = savedInstanceState.getBoolean("state", false)
//            invoke mute button function
            onMuteClick(!state)
        }

//        create handler object
        handler = Handler(Looper.getMainLooper())

//        invoke setExpression function
        setExpression(false)

    }

    /**
     * This Method [setExpression] Generate Random Math expressions
     */
    private fun setExpression(value: Boolean) {
//        enable all disabled buttons
        btnGreater!!.isEnabled = true
        btnEquals!!.isEnabled = true
        btnLess!!.isEnabled = true
        do {
//        invoke getRandomExpression method and assign returning pair to variable
            expOnePair = getRandomExpression()
        } while (expOnePair!!.second < 0 || expOnePair!!.second > 100) // loop if expression value less than 0 or greater than 100
        do {
            expTwoPair = getRandomExpression()
        } while (expTwoPair!!.second < 0 || expTwoPair!!.second > 100)
//        invoke setExpressionToWidgets method
        setExpressionToWidgets(value)
//        Logging data for debugging purpose
        Log.d("EXP1 ${expOnePair!!.first}", "$expOneVal")
        Log.d("EXP2 ${expTwoPair!!.first}", "$expTwoVal")
    }

    /**
     * This Method [setExpressionToWidgets] set expressions to [TextView]'s'
     */
    private fun setExpressionToWidgets(value: Boolean) {
        if (!value) {
            if (savedExpOnePair != null && savedExpTwoPair != null) { // check for null
                if (savedExpOnePair!!.first != "" && savedExpTwoPair!!.first != "") { // check for empty expressions
    //                assign saved pairs to variables
                    expOnePair = savedExpOnePair
                    expTwoPair = savedExpTwoPair
                }
            }
        }
//        set expression one to textView
        expressionOne!!.text = expOnePair!!.first
//        assign value of expression one
        expOneVal = expOnePair!!.second.toInt()
//        set expression two to textView
        expressionTwo!!.text = expTwoPair!!.first
//        assign value of expression two
        expTwoVal = expTwoPair!!.second.toInt()
    }

    /**
     * This Method [getRandomExpression] Generate Random Math expressions with random terms
     * Available at: <https://stackoverflow.com/questions/71225278/how-do-i-generate-a-random-math-expression-using-kotlin> [Accessed 27 February 2022].
     *
     * @return [Pair] of random math expression [String] and its value [Double]
     */
    private fun getRandomExpression(): Pair<String, Double> {
//        list of operators
        val operators = listOf("/", "*", "+", "-").shuffled()
//        generate first number of expression
        val firstTerm = (1..20).random()
//        assign first number to value
        var expVal: Double = firstTerm.toDouble()
//        generate and assign no of terms to variable
        val noOfTerms = (1..3).random()
//        create empty string list to store expression
        val expression = mutableListOf<String>()

        if (noOfTerms >= 2) {
            for (i in 1 until noOfTerms) {
                expression.add("(")
            }
        }

//        add first number to expression list
        expression.add(firstTerm.toString())

        for (i in 1..noOfTerms) {
            if (i >= 2) {
                expression.add(")")
            }
//            get random operator from list
            val operator = operators.random()
//            add operator to expression list
            expression.add(operator)
//            Generate next number
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
            if (operator == "*") {
                var temp: Double
                do {
                    nextTerm = (1..20).random()
                    temp = expVal * nextTerm
                } while (temp >= 100) // generate random number until value is less than 100
            }

//            add next number to expression list
            expression.add(nextTerm.toString())

//            evaluate expressions
            when (operator) {
                "*" -> expVal *= nextTerm
                "/" -> expVal /= nextTerm
                "+" -> expVal += nextTerm
                "-" -> expVal -= nextTerm
            }
        }
//        return pair of expression and its value
        return Pair(expression.joinToString(separator = " "), expVal)
    }

    /**
     * This [getCorrectAnswer] Method Check for Correct Answer
     *
     * @param [operator]
     */
    private fun getCorrectAnswer(operator: String) {
//        increment question count by one
        questionCount++
//        disable all enabled buttons
        btnGreater!!.isEnabled = false
        btnEquals!!.isEnabled = false
        btnLess!!.isEnabled = false

        if ((expOneVal > expTwoVal) && (operator == ">")) {
//            invoke setResultText() method
            setResultText()
        } else if ((expOneVal == expTwoVal) && (operator == "=")) {
            setResultText()
        } else if ((expOneVal < expTwoVal) && (operator == "<")) {
            setResultText()
        } else {
//            set Wrong to text view text
            result!!.text = getString(R.string.wrong)
//            set color of the text view
            result!!.setTextColor(Color.parseColor("#FFFF4444"))
//            set background to text view
            result!!.background = ContextCompat.getDrawable(this, R.drawable.result_bg)
//            tempCorrectCount = 0 //uncomment this for enable consecutive counting
        }
//        Logging data for debugging purpose
        Log.d("EXP QCount", "$questionCount")
        Log.d("EXP CCount", "$correctCount")
        Log.d("EXP TCCount", "$tempCorrectCount")

//        set delay before before next question
        Handler(Looper.getMainLooper()).postDelayed({
            setExpression(true)
//            clear result textView text
            result!!.text = ""
//            clear result textView background
            result!!.background = null
        }, 1500)
    }

    /**
     * This Method [setResultText] set text and background of the result [TextView]
     */
    private fun setResultText() {
//        set Correct to textView text
        result!!.text = getString(R.string.correct)
//            set color of the text view
        result!!.setTextColor(Color.parseColor("#FF99CC00"))
//            set background to text view
        result!!.background = ContextCompat.getDrawable(this, R.drawable.result_bg)
//        increment correct answer count by one
        correctCount++
//        increment temporary correct answer count by
        tempCorrectCount++
    }

    /**
     * This Method [onMuteClick] Mute tick sound
     *
     * @param [btnState]
     */
    private fun onMuteClick(btnState: Boolean) {
        state = when (btnState) {
            true -> {
//                change mute btn icon
                btnMute!!.setImageResource(R.drawable.ic_round_volume_up_48)
//                set MediaPlayer volume to max
                tickPlayer!!.setVolume(1.0F, 1.0F)
//                alter state of the button
                !btnState
            }

            false -> {
                btnMute!!.setImageResource(R.drawable.ic_round_volume_off_48)
//                set MediaPlayer volume to min
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
//                        invoke onMuteClick function
                        onMuteClick(true)
                    }
                    false -> {
//                        invoke onMuteClick function
                        onMuteClick(false)
                    }
                }
            }
        }
    }

    /**
     * This Overridden Method [onSaveInstanceState] save data on activity state change
     *
     * @param [outState]
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        save data
        outState.putInt("time", gameTime)
        outState.putInt("qC", questionCount)
        outState.putInt("cC", correctCount)
        outState.putInt("tCC", tempCorrectCount)
        outState.putString("EXP1F", expOnePair!!.first)
        outState.putString("EXP2F", expTwoPair!!.first)
        outState.putDouble("EXP1S", expOnePair!!.second)
        outState.putDouble("EXP2S", expTwoPair!!.second)
        outState.putBoolean("state", state)
        outState.putInt("tTE", totalTimeElapsed)
    }

    /**
     * This Overridden Method [onPause] remove posts from message queue
     */
    override fun onPause() {
        super.onPause()
//        remove post of the updateTimerText object from message queue
        handler!!.removeCallbacks(updateTimerText)
    }

    /**
     * This Overridden Method [onResume] add posts to the message queue
     */
    override fun onResume() {
        super.onResume()
//        add post of the updateTimerText object to the message queue
        handler!!.post(updateTimerText)
    }

    /**
     * This Method [reduceOneSecond] decrease one second from text
     */
    private fun reduceOneSecond() {
        if (gameTime > 0) {
            if (tempCorrectCount == 5) {
                gameTime += 10
                totalTimeElapsed += 10
                tempCorrectCount = 0
            }
//            decrease one second from gameTime
            gameTime -= 1
//            set time to timer textView
            timerView!!.text = gameTime.toString()
//            set current value to progressBar
            timerBar!!.progress = gameTime
//            play tick sound
            tickPlayer!!.start()
        } else {
//            invoke displayFinishActivity function
            displayFinishActivity()
        }
    }

    /**
     * This Method [displayFinishActivity] display finishActivity
     */
    private fun displayFinishActivity() {
        val finishActivity = Intent(this, FinishActivity::class.java)
//        send data to finishActivity intent
        finishActivity.putExtra("qCount", questionCount)
        finishActivity.putExtra("cCount", correctCount)
        finishActivity.putExtra("tTElapsed", totalTimeElapsed)
//        start finishActivity Intent
        startActivity(finishActivity)
//        destroy current activity
        finish()
    }
}
