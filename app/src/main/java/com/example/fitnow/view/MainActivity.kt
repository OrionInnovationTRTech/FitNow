package com.example.fitnow.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.fitnow.R
import com.example.fitnow.service.MainActivityListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

class MainActivity : AppCompatActivity() , MainActivityListener {
    private lateinit var navController: NavController
    private lateinit var bottomMenu:BottomNavigationView
    var isLoggedIn=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController= navHostFragment.navController
        bottomMenu= findViewById(R.id.bottomNavigationMenu)
        setupWithNavController(bottomMenu,navController)

    }

    override fun onBackPressed() {
        if(!isLoggedIn){
            super.onBackPressed()
        }

    }


    override fun showOrHide(value: Boolean) {
        if(value) bottomMenu.visibility=View.VISIBLE
        else bottomMenu.visibility=View.GONE
    }

    override fun getFilesDirBenim(): File {
        return File(filesDir,"croppedImage.jpg")
    }

    override fun changeBackBtn() {
        isLoggedIn=true
    }


}