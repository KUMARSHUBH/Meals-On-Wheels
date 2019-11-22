package com.krshubham.mealsonwheels.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.ui.userLogin.UserLoginActivity

class SplashScreenActivity : AppCompatActivity() {


    lateinit var auth: FirebaseAuth
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            val user = auth.currentUser
            if (user != null) {
                startActivity(
                    Intent(
                        this, HomeActivity::class.java
                    )
                )
            } else {
                val intent = Intent(applicationContext, UserLoginActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mDelayHandler = Handler()

        auth = FirebaseAuth.getInstance()
        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }
}
