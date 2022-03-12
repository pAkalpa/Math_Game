package me.pasindu.mathgame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.PopupWindow

class MainActivity : AppCompatActivity(), View.OnClickListener {

//    New Game Button Declaration
    private var btnNewGame: Button? = null
//    About Button Declaration
    private var btnAbout: Button? = null
//    PopupWindow Declaration
    private var popupWindow: PopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNewGame = findViewById(R.id.newGame_btn)
        btnAbout = findViewById(R.id.aboutBtn)

//        Set Click Listener for New Game Button
        btnNewGame!!.setOnClickListener(this)
//        Set Click Listener for About Button
        btnAbout!!.setOnClickListener(this)
    }

    /**
     * override [onClick] Method
     * @param view
     */
    override fun onClick(view: View?) {
        when (view?.id) {
            btnNewGame!!.id -> openGameActivity()
            btnAbout!!.id -> openAboutPopUpWindow()
        }
    }

    /**
     * This [openGameActivity] Method Open New Activity [Intent]
     */
    private fun openGameActivity() {
        val gameActivity = Intent(this, GameLogicActivity::class.java)
        startActivity(gameActivity)
    }

    /**
     * This [openAboutPopUpWindow] Method Open [PopupWindow]
     */
    @SuppressLint("InflateParams")
    private fun openAboutPopUpWindow() {
//        Log.d("Clicked", "OpenAboutPopUpWindow Executed")
//        Create LayoutInflater
        val inflater: LayoutInflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        Inflate PopupWindow layout from layouts
        val viewPopUp = inflater.inflate(R.layout.layout_popup, null)
//        Declare PopupWindow properties
        popupWindow = PopupWindow(
            viewPopUp,
            1000,
            700,
            true
        )
//        Declare SlideIn Animation
        val slideIn = Slide()
        slideIn.slideEdge = Gravity.TOP
//        Declare SlideOut Animation
        val slideOut = Slide()
        slideOut.slideEdge = Gravity.END

//        Add Animations to PopupWindow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupWindow!!.enterTransition = slideIn
            popupWindow!!.exitTransition = slideOut
        }

//        Show PopupWindow
        popupWindow!!.showAtLocation(
            viewPopUp, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            80 // Y offset
        )

//        Enable PopupWindow Close on Touch anywhere of the device screen
        popupWindow!!.isOutsideTouchable = true
    }
}