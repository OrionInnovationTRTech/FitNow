package com.example.fitnow.domain

import android.content.Context
import android.util.Log
import com.example.fitnow.data.userOperations.Login

class LoginUseCase(val context:Context) {

    suspend operator fun invoke(email: String, password: String): Boolean {
        Log.e("LoginUseCase",email+password)
        val userLogin = Login(context)
        return userLogin.login(email, password)
    }
}