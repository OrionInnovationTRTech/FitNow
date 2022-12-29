package com.example.fitnow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnow.model.SignUp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel: ViewModel() {
    val registerErrorMessage=MutableLiveData<String>()
    val registerInProgress=MutableLiveData<Boolean>()
    val registerIsSuccess=MutableLiveData<Boolean>()
    private val firebaseAuth= FirebaseAuth.getInstance()


    fun signUpWithFirebase(userInformation:SignUp){
        registerInProgress.value=true
        firebaseAuth
            .createUserWithEmailAndPassword(
                userInformation.email,
                userInformation.password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    registerIsSuccess.value=true
                    addDatabase(userInformation)
                    firebaseAuth.signOut()
                }else{
                    registerIsSuccess.value=false
                    registerErrorMessage.value=it.exception?.message.toString()
                }
                registerInProgress.value=false
            }

    }

    private fun addDatabase(userInformation: SignUp){
        val firebaseDatabase = FirebaseDatabase.getInstance().reference
        val userUID=firebaseAuth.currentUser?.uid.toString()
        firebaseDatabase.child("Users")
            .child(userUID)
            .setValue(userInformation)
        firebaseDatabase.child("Users")
            .child(userUID)
            .child("extra")
            .child("exerciseSituation")
            .setValue("Hiç")
    }
}