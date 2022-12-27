package com.example.fitnow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnow.model.weightHeightIndex

class HealthyViewModel:ViewModel() {
    val user= MutableLiveData<weightHeightIndex>()
    val loading=MutableLiveData<Boolean>()
    val error=MutableLiveData<Boolean>()

    fun getData(){

    }

}