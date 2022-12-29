package com.example.fitnow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UpdateAccountViewModel : ViewModel() {
    val errorMessage=MutableLiveData<String>()
    val loading=MutableLiveData<Boolean>()
    val process=MutableLiveData<String>()
    val response=MutableLiveData<Boolean>()
    val showMain=MutableLiveData<Boolean>()

    private val firebaseAuth=FirebaseAuth.getInstance().currentUser
    private val firebaseDatabase=FirebaseDatabase.getInstance().reference



    fun changePassword(newPassword:String){
        loading.value=true
        firebaseDatabase.child("Users")
            .child(firebaseAuth?.uid.toString())
            .child("password")
            .get().addOnSuccessListener {
                val oldPass=it.value.toString()
                if(newPassword!=oldPass){
                    firebaseAuth?.updatePassword(newPassword)
                        ?.addOnCompleteListener {it2->
                            if (it2.isSuccessful){
                                firebaseDatabase.child("Users")
                                    .child(firebaseAuth.uid)
                                    .child("password")
                                    .setValue(newPassword)

                                process.value="İşlem Başarılı"
                            }else{
                                errorMessage.value=it2.exception?.message.toString()
                            }
                            loading.value=false
                        }
                }else{
                    errorMessage.value="Lütfen mevcut şifrenizden farklı bir şifre giriniz"
                    loading.value=false
                }
            }
            .addOnFailureListener{
                errorMessage.value="Database bağlantısında sorun oluştu, tekrar deneyiniz"
                loading.value=false
            }


    }

    fun changeEmail(newEmail:String){
        loading.value=true
        firebaseDatabase.child("Users")
            .child(firebaseAuth?.uid.toString())
            .child("email")
            .get().addOnSuccessListener {
                val oldEmail=it.value.toString()
                if(newEmail!=oldEmail) {
                    firebaseAuth?.updateEmail(newEmail)
                        ?.addOnCompleteListener {it2->
                            if (it2.isSuccessful) {
                                firebaseDatabase.child("Users")
                                    .child(firebaseAuth.uid)
                                    .child("email")
                                    .setValue(newEmail)
                                process.value = "İşlem Başarılı"
                            } else {
                                errorMessage.value = it2.exception?.message.toString()
                            }
                            loading.value = false
                        }
                }else{
                    errorMessage.value="Lütfen mevcut emailden farklı giriniz"
                    loading.value=false
                }
            }
            .addOnFailureListener {
                errorMessage.value="Database bağlantısında sorun oluştu, tekrar deneyiniz"
                loading.value=false
            }

    }

    fun checkEmailPassword(email:String,password:String){
        loading.value=true
        val signedUserID=firebaseAuth?.uid
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    if (signedUserID==FirebaseAuth.getInstance().currentUser?.uid) {
                        response.value=true
                        showMain.value=false
                    }
                }else{
                    response.value=false
                    errorMessage.value=it.exception?.message.toString()
                    showMain.value=true
                }
                loading.value=false
            }

    }
}