package com.example.fitnow.ui.user

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.fitnow.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordFragment : DialogFragment() {
    lateinit var emailEditText:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_forgot_password, container, false)
        emailEditText=view.findViewById(R.id.emailForgot)

        val btnClose=view.findViewById<Button>(R.id.closeFragment)
        val btnSend=view.findViewById<Button>(R.id.sendLink)

        btnClose.setOnClickListener {
            dismiss()
        }
        btnSend.setOnClickListener {
            if(emailEditText.text.toString()!=""){
                sifreSifirlaBaglantisiYolla()
            }
            else{
                Toast.makeText(activity,"Email Giriniz",Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }

    private fun sifreSifirlaBaglantisiYolla() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(emailEditText.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(activity,"Lütfen E-Mailinizi Kontrol Ediniz",Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                else{
                    Toast.makeText(activity,"Hata="+task.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
    }

}