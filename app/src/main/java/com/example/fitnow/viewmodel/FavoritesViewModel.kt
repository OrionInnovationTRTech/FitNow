package com.example.fitnow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnow.model.FavoritesItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FavoritesViewModel:ViewModel() {
    val itemCount=MutableLiveData<Int>()
    val items=MutableLiveData<ArrayList<FavoritesItem>>()
    val loading=MutableLiveData<Boolean>()

    fun getDatas(){
        itemCount.value=0
        loading.value=true
        items.value= arrayListOf()
        val favList=ArrayList<FavoritesItem>()

        var itemCountNow=0
        val query = FirebaseDatabase.getInstance().reference.child("Users")
            .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
            .child("Favorites")
            .orderByKey()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    if (singleSnapshot.value != null) {
                        val itemId=singleSnapshot.child("itemId").value.toString()
                        val itemName=singleSnapshot.child("itemName").value.toString()
                        val itemImage=singleSnapshot.child("itemImage").value.toString()
                        val itemContent=singleSnapshot.child("itemContent").value.toString()
                        itemCountNow++
                        favList.add(FavoritesItem(itemId,itemName,itemImage,itemContent))
                    }

                }

                itemCount.value=itemCountNow
                items.value=favList
                loading.value=false
            }

            override fun onCancelled(error: DatabaseError) {
                println("Hata Oluştu Database hatası")
            }
        })


    }
}