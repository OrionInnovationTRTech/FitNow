package com.example.fitnow.view

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.fitnow.R
import com.example.fitnow.service.MainActivityListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import kotlin.system.exitProcess

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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(!isLoggedIn){
            super.onBackPressed()
        }else{
            val alert= AlertDialog.Builder(this)
            alert.setTitle("Uygulamadan çıkmak istediğinizden emin misiniz?")
                .setIcon(R.drawable.ic_logout)
                .setPositiveButton("Evet", DialogInterface.OnClickListener{ dialog, which ->
                    FirebaseAuth.getInstance().signOut()
                    exitProcess(-1)
                })
                .setNegativeButton("Hayır", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                .create().show()
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