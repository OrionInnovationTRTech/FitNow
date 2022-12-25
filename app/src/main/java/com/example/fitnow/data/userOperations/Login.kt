package com.example.fitnow.data.userOperations

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.concurrent.fixedRateTimer


class Login(val context:Context) {
    private var response = false
    private var fixedRateTimer: Timer? = null
    suspend fun login(email: String, password: String): Boolean {
        withContext(Dispatchers.IO){
            fixedRateTimer = fixedRateTimer(name = "hello-timer",
                initialDelay = 100, period = 100) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        response = it.isSuccessful
                        Log.e("LoginResponse", response.toString())
                    }
        }
        }
        try {
            withContext(Dispatchers.IO) {
                Thread.sleep(1000)
            }
        } finally {
            fixedRateTimer?.cancel()
        }
        Log.e("enaltrespone çalıştı", response.toString())
     return response
    }
}



