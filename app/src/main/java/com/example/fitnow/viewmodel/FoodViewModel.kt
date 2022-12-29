package com.example.fitnow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnow.data.entity.MealData
import com.example.fitnow.model.*
import com.example.fitnow.service.FoodAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FoodViewModel: ViewModel() {
    var foodList=MutableLiveData<List<MealData.Results>>()
    var foodError=MutableLiveData<Boolean>()
    var foodLoading=MutableLiveData<Boolean>()

    lateinit var foodAPIService:FoodAPI
    lateinit var queryListener:List<MealData.SearchResults>

    fun refreshData(query: String){
        foodError.value=false
        foodLoading.value=true
        createRetrofit()
        val apiCall=foodAPIService.getFoodDetails(query, API_KEY, SEARCH_LIMIT)
        apiCall.enqueue(object : Callback<MealData?> {
            override fun onResponse(call: Call<MealData?>, response: Response<MealData?>) {
                queryListener= response.body()?.searchResults!!
                if(queryListener[0].totalResults.toString()=="0"){
                    foodLoading.value=false
                    foodError.value=true
                }else {
                    foodLoading.value=false
                    foodList.value = queryListener[0].results
                }

            }

            override fun onFailure(call: Call<MealData?>, t: Throwable) {
                println("Hata: ${t.stackTraceToString()}")
            }
        })
    }


    private fun createRetrofit(){
        foodAPIService=Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(FoodAPI::class.java)
    }



}