package com.example.fitnow.viewmodel


import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnow.model.SettingsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.system.exitProcess

class SettingsViewModel : ViewModel() {
    val emailSituation=MutableLiveData<String>()
    val userDetails=MutableLiveData<SettingsModel>()
    val loading=MutableLiveData<Boolean>()
    val errorMessage=MutableLiveData<String>()
    val spinnerList= MutableLiveData<List<String>>()
    val exercise=MutableLiveData<String>()
    val update=MutableLiveData<Boolean>()

    private val firebaseAuth = FirebaseAuth.getInstance().currentUser
    private val firebaseDatabase = FirebaseDatabase.getInstance().reference


    fun fillDatas() {
        // TODO(Kadın seçiliyken her girişte güncelleme başarılı vs yazıyor ona göz at)
        loading.value = true
        val query = firebaseDatabase.child("Users")
            .orderByKey()
            .equalTo(firebaseAuth?.uid.toString())
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    val oldHeight = singleSnapshot.child("extra").child("height").value.toString()
                    val oldAge = singleSnapshot.child("extra").child("age").value.toString()
                    val oldWeight = singleSnapshot.child("extra").child("weight").value.toString()
                    val oldJob = singleSnapshot.child("extra").child("job").value.toString()
                    val oldEx = singleSnapshot.child("extra").child("exerciseSituation").value.toString()
                    fillSpinner(oldEx)
                    val oldGender = singleSnapshot.child("extra").child("gender").value.toString()
                    if (oldJob != "null")
                        userDetails.value = SettingsModel(
                            oldHeight,
                            oldWeight,
                            oldAge,
                            oldJob,
                            oldEx,
                            oldGender
                        )
                    loading.value = false

                }
            }

            override fun onCancelled(error: DatabaseError) {
                errorMessage.value = "Hata: ${error.message}"
            }
        })
    }
    fun fillSpinner(exercise:String){
        when(exercise) {
            "Hiç"-> {
                spinnerList.value= arrayListOf("Hiç", "1-2 Kez", "3-5 Kez", "7 Kez", "7+ Kez")
            }
            "1-2 Kez" -> {
                spinnerList.value= arrayListOf(exercise, "Hiç", "3-5 Kez", "7 Kez", "7+ Kez")
            }
            "3-5 Kez" -> {
                spinnerList.value=arrayListOf(exercise, "Hiç", "1-2 Kez", "7 Kez", "7+ Kez")
            }
            "7 Kez" -> {
                spinnerList.value= arrayListOf(exercise, "Hiç","1-2 Kez","3-5 Kez", "7+ Kez")
            }
            "7+ Kez" -> {
                spinnerList.value= arrayListOf(exercise, "Hiç", "1-2 Kez", "3-5 Kez", "7 Kez")
            }

        }
    }

    fun updateUser(newUser:SettingsModel,it:View) {
        loading.value=true
        firebaseDatabase
            .child("Users")
            .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
            .child("extra")
            .setValue(newUser).addOnCompleteListener {
                if (it.isSuccessful) update.value=true
                else errorMessage.value= "İşlem başarısız"
                loading.value=false
            }
    }
    fun deleteAccount(){
        loading.value=true

         firebaseDatabase.child("Users")
            .child(firebaseAuth?.uid.toString())
             .removeValue()
        firebaseAuth?.delete()?.addOnCompleteListener {
            if (it.isSuccessful) {
                errorMessage.value="Silme işlemi başarılı"
                exitProcess(-1)
            }
            else errorMessage.value="Silme işlemi başarısız"
            loading.value=false
        }
    }


    fun verifiedEmail() {
        if (firebaseAuth != null) {
                if (firebaseAuth.isEmailVerified) {
                    emailSituation.value = "Email durumunuz onaylı"
                } else {
                    firebaseAuth.sendEmailVerification()
                        .addOnCompleteListener {
                            if (it.isSuccessful) emailSituation.value =
                                "Lütfen e-mailinizi kontrol edip onaylayın ve tekrar deneyiniz."
                            else emailSituation.value = "Hata. ${it.exception?.message}"
                        }
                }
            } else {
                emailSituation.value = "Lütfen giriş yapıp tekrar deneyin"
            }
        // TODO("getStringleri düzenle verifiedEmail Fonksiyonu içerisi")
    }
}