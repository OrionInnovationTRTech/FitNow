package com.example.fitnow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnow.model.OneFoodItem

class OneFoodViewModel:ViewModel() {
    val foodLiveData=MutableLiveData<OneFoodItem>()

    fun getData(foodDetails:OneFoodItem){
        foodLiveData.value=foodDetails
    }
}