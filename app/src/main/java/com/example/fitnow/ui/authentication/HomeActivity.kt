package com.example.fitnow.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.fitnow.ui.user.ForgotPasswordFragment
import com.example.fitnow.R
import com.example.fitnow.ui.user.UserHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.btn_giris
import kotlinx.android.synthetic.main.activity_home.girisEmail


class HomeActivity : AppCompatActivity() {
    lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        myAuthStateListener()
        auth= Firebase.auth
        initiliazeUI()

    }

    private fun initiliazeUI() {
        TODO("Not yet implemented")
    }

    //Auth state listener methodu activity ile beraber çağırılır. Bu method içerisinde uygulamaya giriş
    // yapmış kullanıcı olup olmadığı kontrol edilir. Eğer daha önce giriş yapılmış ise otomatik
    // giriş sayfasına yönlendirmesini bu metot ile sağladım.
    private fun myAuthStateListener() {
        mAuthStateListener = FirebaseAuth.AuthStateListener { p0 ->
            val kullanici = p0.currentUser
            if (kullanici!= null){
                startActivity(
                    Intent(
                        this@HomeActivity,
                        UserHomeActivity::class.java
                    )
                )

            }
        }
    }




    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener)
    }
}