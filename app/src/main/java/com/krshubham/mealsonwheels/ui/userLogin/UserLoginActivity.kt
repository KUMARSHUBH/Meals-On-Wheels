package com.krshubham.mealsonwheels.ui.userLogin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.ui.HomeActivity

class UserLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()


        createSignInIntent()
    }


    private fun createSignInIntent() {

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.LoginTheme)
                .setLogo(R.drawable.logo)
                .build(),
            RC_SIGN_IN
        )

    }

    override fun onStart() {
        super.onStart()

        val user = auth.currentUser
        if(user != null){
            startActivity(Intent(this, HomeActivity::class.java))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in

                startActivity(Intent(this, HomeActivity::class.java))


            } else {

            }
        }
    }

    companion object {

        private const val RC_SIGN_IN = 123
    }
}
