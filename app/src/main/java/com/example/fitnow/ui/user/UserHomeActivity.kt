package com.example.fitnow.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fitnow.R
import com.example.fitnow.ui.BottomMenuFragments.CalorieFragment
import com.example.fitnow.ui.BottomMenuFragments.FastFoodFragment
import com.example.fitnow.ui.BottomMenuFragments.FavoritesFragment
import com.example.fitnow.ui.BottomMenuFragments.ProfileFragment
import com.example.fitnow.ui.authentication.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_home.*


class UserHomeActivity : AppCompatActivity() {
    lateinit var myAuthStateListener: FirebaseAuth.AuthStateListener
    private val profileFragment = ProfileFragment()
    private val calorieFragment = CalorieFragment()
    private val favoritesFragment = FavoritesFragment()
    private val fastFoodFragment = FastFoodFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)
        initAuthStateListener()
        replaceFragment(fastFoodFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_account -> replaceFragment(profileFragment)
                R.id.ic_calorie -> replaceFragment(calorieFragment)
                R.id.ic_fastfood -> replaceFragment(fastFoodFragment)
                R.id.ic_favorite -> replaceFragment(favoritesFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

    private fun initAuthStateListener() {
        myAuthStateListener = FirebaseAuth.AuthStateListener { p0 ->
            val kullanici = p0.currentUser
            if (kullanici == null) {
                val intent = Intent(this@UserHomeActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.anamenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuCikisYap -> {
                cikisYap()
                return true
            }
            R.id.emailOnayla -> {
                verifiedEmail()
                return true
            }
            R.id.menuHesapAyarlari -> {
                hesapBilgileriGoster()
                return true
            }
            R.id.menuSifreDegistir -> {
                mailSifreDegistir()
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun verifiedEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            if (user.isEmailVerified) {
                Toast.makeText(
                    this,
                    "E-Mail durumunuz onaylı.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                user.sendEmailVerification()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Lütfen e-mailinizi kontrol ediniz.",
                                Toast.LENGTH_LONG
                            ).show()
                            FirebaseAuth.getInstance().signOut()
                            finish()

                        } else {
                            Toast.makeText(
                                this,
                                "Hata. " + it.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        } else {
            Toast.makeText(this, "Lütfen giriş yapıp tekrar deneyin", Toast.LENGTH_SHORT).show()
        }
    }


    private fun mailSifreDegistir() {
        val showDialogFragment = updateAccountFragment()
        showDialogFragment.show(supportFragmentManager, "updateAccountFragment")
    }

    private fun hesapBilgileriGoster() {
        startActivity(Intent(this@UserHomeActivity, UserinformationActivity::class.java))
    }

    private fun cikisYap() {
        FirebaseAuth.getInstance().signOut()
    }

    override fun onStart() {
        FirebaseAuth.getInstance().addAuthStateListener(myAuthStateListener)
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        if (myAuthStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(myAuthStateListener)
        }
    }  // önemsiz uyarı dinleme, düzeltme.

    override fun onResume() {
        super.onResume()
        val kullanici = FirebaseAuth.getInstance().currentUser

        if (kullanici == null) {
            val intent = Intent(this@UserHomeActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }


}