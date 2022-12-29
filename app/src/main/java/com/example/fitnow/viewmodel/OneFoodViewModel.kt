package com.example.fitnow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnow.data.entity.MealData
import com.example.fitnow.model.IMAGE_APPLE

class OneFoodViewModel:ViewModel() {
    val foodLiveData=MutableLiveData<MealData.Results>()

    fun getData(foodDetails:MealData.Results){
        foodLiveData.value=foodDetails
    }
}