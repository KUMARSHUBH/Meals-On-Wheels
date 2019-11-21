package com.krshubham.mealsonwheels.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.krshubham.mealsonwheels.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_food,
                R.id.navigation_search,
                R.id.navigation_cart,
                R.id.navigation_video
            )
        )
        navView.setupWithNavController(navController)


        nav_button.setOnClickListener {

            if (!drawer_layout.isDrawerOpen(GravityCompat.END))
                drawer_layout.openDrawer(GravityCompat.END)
            else
                drawer_layout.closeDrawer(GravityCompat.END)
        }


    }




}
