package com.example.fitnow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnow.model.SignIn
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    val loginErrorMessage = MutableLiveData<String>()
    val loginInProgress = MutableLiveData<Boolean>()
    val loginIsSuccess = MutableLiveData<Boolean>()

    fun signInWithFirebase(userInformation:SignIn){
        loginInProgress.value=true
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            userInformation.userEmail,
            userInformation.userPassword)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    loginIsSuccess.value=true
                }else{
                    loginIsSuccess.value=false
                    loginErrorMessage.value=it.exception?.message.toString()
                }
                loginInProgress.value=false
            }
    }

}