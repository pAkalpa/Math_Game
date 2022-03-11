package me.pasindu.mathgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        this.supportActionBar!!.hide()
    }
}