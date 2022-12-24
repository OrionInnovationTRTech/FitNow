package com.example.fitnow.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.fitnow.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_update_account.*

class updateAccountFragment : DialogFragment() {
    lateinit var emailTextview: TextView
    lateinit var btnPassChange: Button
    lateinit var sifreTextview: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newPassBTN.setOnClickListener {
            updatePassword()
        }
        changeMail.setOnClickListener {
            changeUserMail()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_account, container, false)
        val kullanici = FirebaseAuth.getInstance()
        emailTextview = view.findViewById(R.id.emailEditText)
        sifreTextview = view.findViewById(R.id.passEditText)
        btnPassChange = view.findViewById(R.id.changePass)
        val btnMailChange = view.findViewById<Button>(R.id.changeMail)
        emailTextview.text = kullanici.currentUser?.email
        btnPassChange.setOnClickListener {

            if (passEditText.text.toString().isNotEmpty()) {
                val credential = EmailAuthProvider.getCredential(
                    kullanici.currentUser?.email.toString(),
                    passEditText.text.toString()
                )
                kullanici.currentUser?.reauthenticate(credential)
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            btnPassChange.isEnabled = false
                            passChangeLayout.visibility = View.VISIBLE
                        } else {
                            Toast.makeText(
                                activity,
                                "Hata=" + it.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            } else {
                Toast.makeText(activity, "Lütfen şuanki şifrenizi giriniz", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return view
    }


    private fun updatePassword() {
        val kullanici = FirebaseAuth.getInstance().currentUser
        if (kullanici != null) {
            kullanici.updatePassword(newPassEditText.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    FirebaseDatabase.getInstance().reference.child("kullanici").child(kullanici.uid)
                        .child("password")
                        .setValue(newPassEditText.text.toString())
                    passChangeLayout.visibility = View.INVISIBLE
                    btnPassChange.isEnabled = true
                    passEditText.setText("")
                    Toast.makeText(activity, "Şifre değişimi başarılı", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        activity,
                        "Şifre değişimi başarısız. " + it.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        } else {
            Toast.makeText(activity, "Şifreniz değiştirilemedi.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun changeUserMail() {

        val kullanici = FirebaseAuth.getInstance()
        if (passEditText.text.toString().isNotEmpty()) {
            val credential = EmailAuthProvider.getCredential(
                kullanici.currentUser?.email.toString(),
                passEditText.text.toString()
            )
            kullanici.currentUser?.reauthenticate(credential)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        kullanici.currentUser!!.updateEmail(emailEditText.text.toString())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    FirebaseDatabase.getInstance().reference.child("kullanici").child(
                                        kullanici.currentUser!!.uid)
                                        .child("email")
                                        .setValue(emailEditText.text.toString())
                                    Toast.makeText(
                                        activity,
                                        "E-Mail değişimi başarılı",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "E-Mail değişimi başarısız." + task.exception?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                    } else {
                        Toast.makeText(
                            activity,
                            "Hatalı şifre, lütfen şifrenizi giriniz.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        else{
            Toast.makeText(
                activity,
                "Lütfen şifrenizi giriniz.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}