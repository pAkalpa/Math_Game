package me.pasindu.mathgame

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.close
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var btnNewGame: Button? = null
    private var btnAbout: Button? = null
    private var popupWindow: PopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNewGame = findViewById(R.id.newGame_btn)
        btnAbout = findViewById(R.id.aboutBtn)

        btnNewGame!!.setOnClickListener(this)
        btnAbout!!.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            btnNewGame!!.id -> openGameActivity()
            btnAbout!!.id -> openAboutPopUpWindow(view)
        }
    }

    private fun openGameActivity() {
//        TODO: Implement Open GameLogicActivity Intent
    }

    private fun openAboutPopUpWindow(view: View) {
        Log.d("Clicked","OpenAboutPopUpWindow Executed")
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewPopUp = inflater.inflate(R.layout.layout_popup,null)
        popupWindow = PopupWindow(
            viewPopUp,
            1300,
            700,
            true
        )
        val slideIn = Slide()
        slideIn.slideEdge = Gravity.TOP
        val slideOut = Slide()
        slideOut.slideEdge = Gravity.END

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupWindow!!.enterTransition = slideIn
            popupWindow!!.exitTransition = slideOut
        }

        popupWindow!!.showAtLocation(
            viewPopUp, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            0 // Y offset
        )

        popupWindow!!.isOutsideTouchable = true
    }
}