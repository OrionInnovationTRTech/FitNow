package com.example.fitnow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnow.model.FavoritesItem
import com.example.fitnow.model.OneFoodItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class OneFoodViewModel:ViewModel() {
    val foodLiveData=MutableLiveData<OneFoodItem>()
    val response=MutableLiveData<String>()
    val loading=MutableLiveData<Boolean>()

    private val firebaseAuth=FirebaseAuth.getInstance().currentUser
    private val firebaseDatabase=FirebaseDatabase.getInstance().reference

    fun getData(foodDetails:OneFoodItem){
        checkFav(foodDetails.itemName)
        foodLiveData.value=foodDetails
    }

    private fun checkFav(itemName:String){
        firebaseDatabase.child("Users")
            .child(firebaseAuth?.uid.toString())
            .child("Favorites")
            .child(itemName)
            .get()
            .addOnSuccessListener {
                if(it.value!=null) response.value="Fav"
                else response.value="NotFav"
            }
            .addOnFailureListener{
                println("Hata: ${it.message.toString()}")
            }


    }

    fun deleteOrFavFood(isChecked:Boolean,itemContent:String){
        val foodInfo=foodLiveData.value
        val newFood=FavoritesItem(
            foodInfo?.itemId.toString(),
            foodInfo?.itemName.toString(),
            foodInfo?.itemImage.toString(),
            itemContent)
        if(isChecked){
            firebaseDatabase
                .child("Users")
                .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                .child("Favorites")
                .child(newFood.itemName)
                .setValue(newFood)
        }else{
            firebaseDatabase
                .child("Users")
                .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                .child("Favorites")
                .child(newFood.itemName)
                .removeValue()
        }
    }
}