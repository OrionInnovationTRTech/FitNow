package com.example.fitnow.model

import com.google.gson.annotations.SerializedName

data class SignIn(val userEmail:String, val userPassword:String)
data class SignUp(val nameSurname:String,
                  val username:String,
                  val email:String,
                  val password:String)

data class weightHeightIndex(val weight:Int, val height:Int)


data class FavoritesItem(
    var itemId: String,
    var itemName: String,
    var itemImage: String)
