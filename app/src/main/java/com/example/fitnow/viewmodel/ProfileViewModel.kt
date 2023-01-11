package com.example.fitnow.viewmodel

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnow.model.ProfileModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileViewModel:ViewModel() {
    val userInformation=MutableLiveData<ProfileModel>()
    val isHaveInput=MutableLiveData<Boolean>()
    val loading=MutableLiveData<Boolean>()
    val errorMessage=MutableLiveData<String>()
    val selectingImage=MutableLiveData<Uri>()

    private val firebaseAuth= FirebaseAuth.getInstance().currentUser
    private val firebaseDatabase= FirebaseDatabase.getInstance().reference

    fun getData(){
        loading.value=true
        isHaveInput.value=false
        val query = firebaseDatabase.child("Users")
            .orderByKey()
            .equalTo(firebaseAuth?.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    val userName = singleSnapshot.child("nameSurname").value.toString()
                    val userAge = singleSnapshot.child("extra").child("age").value.toString()
                    val userWeight = singleSnapshot.child("extra").child("weight").value.toString()
                    val userHeight = singleSnapshot.child("extra").child("height").value.toString()
                    val userJob = singleSnapshot.child("extra").child("job").value.toString()
                    val userExercise = singleSnapshot.child("extra").child("exerciseSituation").value.toString()
                    val profilePictureURL = singleSnapshot.child("imageURL").value.toString()
                    if (userAge != "null" || userHeight != "null" || userWeight != "null") {
                        isHaveInput.value=true
                        userInformation.value=ProfileModel(userName,userAge,userHeight,userWeight,userJob,userExercise, profilePictureURL)
                    }
                }
                loading.value=false
                if(isHaveInput.value!=true) isHaveInput.value=false
            }
            override fun onCancelled(error: DatabaseError) {
                println("Hata: ${error.message} ${error.toException()}")
                errorMessage.value="Hata: ${error.message} ${error.toException()}"
                loading.value=false
            }
        })
    }

    fun uploadImage(path: Uri,context:Context){
            val imageRef =
                FirebaseStorage.getInstance().reference.child("${firebaseAuth?.uid}/images/profilepicture.jpg")
            val pd = ProgressDialog(context)
            pd.setTitle("Resminiz hazırlanıyor")
            pd.show()
            imageRef.putFile(path)
                .addOnSuccessListener {
                    pd.dismiss()
                    imageRef.downloadUrl
                        .addOnCompleteListener {
                            val imageDatabaseURL = it.result.toString()
                            FirebaseDatabase.getInstance().reference
                                .child("Users")
                                .child(firebaseAuth?.uid.toString())
                                .child("imageURL")
                                .setValue(imageDatabaseURL)
                        }
                }
                .addOnFailureListener {
                    pd.dismiss()
                    println("Failed. ${it.message}")
                }
                .addOnProgressListener {
                    val progress = (100 * it.bytesTransferred) / it.totalByteCount
                    pd.setMessage("Uploaded: ${progress.toInt()}%")
                }
    }
}