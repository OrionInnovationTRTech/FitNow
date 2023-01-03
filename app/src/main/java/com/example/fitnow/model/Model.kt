package com.example.fitnow.model

import android.text.Spanned


data class SignIn(val userEmail:String, val userPassword:String)
data class SignUp(val nameSurname:String,
                  val username:String,
                  val email:String,
                  val password:String,
                  val imageURL: String)

data class WeightHeightIndex(val formattedIndex:String?, val whIndex:Double)
data class ProfileModel(val nameSurname: String,
                        val age:String,
                        val height:String,
                        val weight:String,
                        val job:String,
                        val exercise:String,
                        val imageURL:String)
data class SettingsModel(val height: String,
                        val weight: String,
                        val age: String,
                        val job: String,
                        val exerciseSituation: String,
                        val gender:String)

data class FavoritesItem(
    val itemId: String,
    val itemName: String,
    val itemImage: String,
    val itemContent:String)

data class OneFoodItem(
    val itemId: String,
    val itemName: String,
    val itemImage: String,
    val parseContent:Spanned)
