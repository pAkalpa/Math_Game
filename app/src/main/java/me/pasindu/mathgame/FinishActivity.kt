package me.pasindu.mathgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView

class FinishActivity : AppCompatActivity() {

    //    questionCount text view declaration
    private var questionCountV: TextView? = null

    //    total elapsed time text view declaration
    private var ttElapsedV: TextView? = null

    //    correct count text view declaration
    private var caCountV: TextView? = null

    //    incorrect count text view declaration
    private var iaCountV: TextView? = null

    //    question Count
    private var questionCount: Int? = null

    //    correct count
    private var correctCount: Int? = null

    //    total elapsed time
    private var totalTimeElapsed: Int? = null

    //    frame define variable
    private var frame: LinearLayout? = null

    //    vertical animation variable
    private var verticalAnimation: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

//        hide action Bar
        this.supportActionBar!!.hide()

//        Assign id's to variables
        questionCountV = findViewById(R.id.qCountV)
        ttElapsedV = findViewById(R.id.ttElapsedV)
        caCountV = findViewById(R.id.caCountV)
        iaCountV = findViewById(R.id.iaCountV)
        frame = findViewById(R.id.linearLayout)
        verticalAnimation = AnimationUtils.loadAnimation(this, R.anim.verticalmove)

//        retrieve extras
        questionCount = intent.getIntExtra("qCount", 0)
        correctCount = intent.getIntExtra("cCount", 0)
        totalTimeElapsed = intent.getIntExtra("tTElapsed", 0)

//        Retrieve data from savedInstanceState
        if (savedInstanceState != null) {
//            load question Count
            questionCount = savedInstanceState.getInt("qc", 0)
//            load correct count
            correctCount = savedInstanceState.getInt("cc", 0)
//            load total time elapsed
            totalTimeElapsed = savedInstanceState.getInt("te", 0)
        }

        frame!!.startAnimation(verticalAnimation)

//        invoke setContent function
        setContent()
    }

    /**
     * This Method [setContent] set data to [TextView]'s'
     */
    private fun setContent() {
        questionCountV!!.text = questionCount.toString()
        ttElapsedV!!.text = getString(R.string.timeAnswer, totalTimeElapsed)
        caCountV!!.text = correctCount.toString()
        iaCountV!!.text = (questionCount!! - correctCount!!).toString()
    }

    /**
     * This Overridden Method [onSaveInstanceState] save data on activity state change
     *
     * @param [outState]
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("qc", questionCount!!)
        outState.putInt("cc", correctCount!!)
        outState.putInt("te", totalTimeElapsed!!)
    }
}