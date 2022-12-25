package com.example.fitnow.domain

import android.content.Context
import android.util.Log
import com.example.fitnow.data.entity.User
import com.example.fitnow.data.userOperations.Register

class RegisterUseCase(val context: Context) {

    suspend operator fun invoke(user:User):Boolean{
        val userRegister= Register(context)
        return userRegister.register(user)
    }
}