package com.example.fitnow.ui.authentication

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fitnow.data.entity.User
import com.example.fitnow.domain.RegisterUseCase
import kotlinx.coroutines.launch


class RegisterViewModel(application: Application):AndroidViewModel(application){
    private val _uiState: MutableLiveData<UIState> = MutableLiveData(UIState.IDLE)
    val uiState:LiveData<UIState> = _uiState

    private val registerUseCase= RegisterUseCase(getApplication<Application>().applicationContext)

    fun register(user:User){
        viewModelScope.launch {
            _uiState.postValue(UIState.IN_PROGRESS)
            val isRegisterSuccessful = registerUseCase(user)
            if (isRegisterSuccessful) _uiState.postValue(UIState.REGISTER_SUCCESS)
            else _uiState.postValue(UIState.REGISTER_FAILED)
        }

    }

    enum class UIState {
        IDLE,
        IN_PROGRESS,
        REGISTER_SUCCESS,
        REGISTER_FAILED
    }
}