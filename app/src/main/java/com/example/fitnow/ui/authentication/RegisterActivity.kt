package com.example.fitnow.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.fitnow.R
import com.example.fitnow.data.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            if (registerName.text.isEmpty() || registerUsername.text.isEmpty() ||
                registerEmail.text.isEmpty() || registerPassword.text.isEmpty() ||
                registerPassword2.text.isEmpty()) {
                Toast.makeText(this, "Boş alanları doldurunuz", Toast.LENGTH_SHORT).show()
            } else {
                if (registerPassword.text.toString() == registerPassword2.text.toString()) {
                    showProgressBar()
                    newUser(
                        registerName.text.toString(),
                        registerUsername.text.toString(),
                        registerEmail.text.toString(),
                        registerPassword.text.toString()
                    )

                } else {
                    Toast.makeText(this, "Şifreler uyuşmuyor.", Toast.LENGTH_SHORT).show()
                }
            }
            hideProgressBar()
        }
        btn_back.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }


    }

    private fun newUser(name: String, username: String, email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { p0 ->
                if (p0.isSuccessful) {
                    val newUserDatas = User()
                    newUserDatas.kullanici_id = FirebaseAuth.getInstance().currentUser?.uid
                    newUserDatas.email = email
                    newUserDatas.isim = name
                    newUserDatas.username = username
                    newUserDatas.password = password

                    FirebaseDatabase.getInstance().reference
                        .child("kullanici")
                        .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                        .setValue(newUserDatas).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this@RegisterActivity, "Kayıt başarılı", Toast.LENGTH_SHORT).show()
                                startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        HomeActivity::class.java
                                    )
                                )

                            }
                        }
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Kayıt başarısız oldu\n" + p0.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun showProgressBar() {
        progressBarRegister.visibility = View.VISIBLE
        val pbar= findViewById<ProgressBar>(R.id.progressBarRegister)
        pbar.visibility=View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBarRegister.visibility = View.INVISIBLE
        val pbar= findViewById<ProgressBar>(R.id.progressBarRegister)
        pbar.visibility=View.INVISIBLE
    }


}