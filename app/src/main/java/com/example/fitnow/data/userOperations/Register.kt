package com.example.fitnow.data.userOperations

import android.content.Context
import android.util.Log
import com.example.fitnow.data.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

class Register(val context: Context) {
    private var response = false
    private var fixedRateTimer: Timer? =null
    suspend fun register(user: User):Boolean{
        withContext(Dispatchers.IO){
            fixedRateTimer= fixedRateTimer(name = "hello-timer",
                initialDelay = 100, period = 100){
                if (user.password != null && user.email!=null) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email!!, user.password!!)
                        .addOnCompleteListener { process ->
                            if (process.isSuccessful) {
                                user.kullanici_id = FirebaseAuth.getInstance().currentUser?.uid
                                FirebaseDatabase.getInstance().reference
                                    .child("kullanici")
                                    .child(user.kullanici_id.toString())
                                    .setValue(user)
                                response=true
                            }
                        }
                }
            }
            try {
                withContext(Dispatchers.IO){
                    Thread.sleep(2000) // firebase bağlantısı yavaş olduğundan bekleme koyuyorum.
                    // internet hızından mı değil mi bilmiyorum yaklaşık 1220 millis sürüyor işlem
                }
            }finally {
                fixedRateTimer?.cancel()
            }
        }
        return response
    }
}