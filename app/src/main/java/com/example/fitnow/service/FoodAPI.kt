package com.example.fitnow.service


import com.example.fitnow.data.entity.MealData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodAPI {

    @GET("search")
    fun getFoodDetails(@Query("query") query:String, @Query("apiKey") apiKey:String, @Query("limit") limit:Int): Call<MealData>

}