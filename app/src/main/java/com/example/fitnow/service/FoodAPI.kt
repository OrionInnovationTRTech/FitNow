package com.example.fitnow.service


import com.example.fitnow.data.entity.MealData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodAPI {
    //https://api.spoonacular.com/food/search?query=apple&apiKey=d770324ab7404c3981b13dc2918745cb

    @GET("search")
    fun getFoodDetails(@Query("query") query:String, @Query("apiKey") apiKey:String, @Query("limit") limit:Int): Call<MealData>

}