package com.example.fitnow.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController

import com.example.fitnow.R
import com.example.fitnow.service.MenuListener
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() , MenuListener {
    private lateinit var navController: NavController
    private lateinit var bottomMenu:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController= navHostFragment.navController
        bottomMenu= findViewById(R.id.bottomNavigationMenu)
        setupWithNavController(bottomMenu,navController)

    }

    override fun showOrHide(value: Boolean) {
        if(value) bottomMenu.visibility=View.VISIBLE
        else bottomMenu.visibility=View.GONE
    }


}