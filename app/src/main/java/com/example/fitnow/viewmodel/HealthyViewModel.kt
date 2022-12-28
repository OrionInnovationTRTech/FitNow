package com.example.fitnow.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnow.model.WeightHeightIndex
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat

class HealthyViewModel:ViewModel() {
    val user= MutableLiveData<WeightHeightIndex>()
    val loading=MutableLiveData<Boolean>()
    val isHaveInput=MutableLiveData<Boolean>()

    private val firebaseAuth=FirebaseAuth.getInstance().currentUser
    private val firebaseDatabase=FirebaseDatabase.getInstance().reference

    fun getData(){
        loading.value=true
        isHaveInput.value=false
        val query = firebaseDatabase.child("Users")
            .orderByKey()
            .equalTo(firebaseAuth?.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    if(singleSnapshot.child("extra").child("kilo").value!=null) {
                        isHaveInput.value=true
                        val userWeight =
                            singleSnapshot.child("extra").child("kilo").value.toString().toDouble()
                        val userHeight =
                            singleSnapshot.child("extra").child("boy").value.toString().toDouble()
                        val index: Double = userWeight / ((userHeight * userHeight) / 10000)
                        val lastIndex = writeIndex(index)
                        user.value = WeightHeightIndex(lastIndex, index)
                    }
            }
                loading.value=false
                if(isHaveInput.value!=true) isHaveInput.value=false

            }

            override fun onCancelled(error: DatabaseError) {
                println("loadPost:onCancelled "+ error.toException())
            }

        })
    }

    private fun writeIndex(index: Double) : String {
        val df= DecimalFormat("#.##");      //Format çeşidi belirtiliyor.#.## noktadan sonra kaç diyez var ise.
        return df.format(index);                   // x sayımız formatlanarak döndürülüyor.
    }

}