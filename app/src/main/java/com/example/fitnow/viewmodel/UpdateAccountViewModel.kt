package com.example.fitnow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class UpdateAccountViewModel : ViewModel() {
    val errorMessage=MutableLiveData<String>()
    val loading=MutableLiveData<Boolean>()
    val process=MutableLiveData<String>()
    val response=MutableLiveData<Boolean>()

    private val firebaseAuth=FirebaseAuth.getInstance().currentUser
    private val firebaseDatabase=FirebaseDatabase.getInstance().reference



    fun changePassword(newPassword:String){
        loading.value=true
        val oldPass=firebaseDatabase
            .child("Users")
            .child(firebaseAuth?.uid.toString())
            .child("password")
            .get()
        println(oldPass.toString())
       // if(newPassword!=oldPass.toString())
        firebaseAuth?.updatePassword(newPassword)
            ?.addOnCompleteListener {
                if (it.isSuccessful){
                    firebaseDatabase.child("Users")
                        .child(firebaseAuth.uid)
                        .child("password")
                        .setValue(newPassword)


                    process.value="İşlem Başarılı"
                }else{
                    errorMessage.value=it.exception?.message.toString()
                }
                loading.value=false
            }
    }

    fun changeEmail(newEmail:String){
        loading.value=true
        firebaseAuth?.updateEmail(newEmail)
            ?.addOnCompleteListener {
                if (it.isSuccessful){
                    firebaseDatabase.child("Users")
                        .child(firebaseAuth.uid)
                        .child("email")
                        .setValue(newEmail)
                    process.value="İşlem Başarılı"
                }else{
                    errorMessage.value=it.exception?.message.toString()
                }
                loading.value=false
            }

    }

    fun checkEmailPassword(email:String,password:String){
        loading.value=true
        val signedUserID=firebaseAuth?.uid
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    if (signedUserID==FirebaseAuth.getInstance().currentUser?.uid) response.value=true
                }else{
                    response.value=false
                    errorMessage.value=it.exception?.message.toString()
                }
                loading.value=false
            }

    }
}