package com.krshubham.mealsonwheels.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.krshubham.mealsonwheels.R
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetail : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var databseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        firebaseAuth = FirebaseAuth.getInstance()
        databseReference = FirebaseDatabase.getInstance().getReference("user").child(firebaseAuth.uid.toString())

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.title = ""


        profile_continue.setOnClickListener {

            databseReference.apply {
                updateChildren(mapOf("name" to  edit_name.text.toString()))
                child("phone").setValue(phone_no.text.toString())

                finish()
            }
        }
    }

    override fun onNavigateUp(): Boolean {

        finish()

        return true

    }
}
