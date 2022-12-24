package com.example.fitnow.ui.authentication

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fitnow.domain.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel(application: Application):AndroidViewModel(application) {

    private val _uiState:MutableLiveData<UiState> = MutableLiveData(UiState.IDLE)
    val uiState:LiveData<UiState> = _uiState



    private val loginUseCase= LoginUseCase(getApplication<Application>().applicationContext)

    fun login(email:String, password:String){
        viewModelScope.launch {
            _uiState.postValue(UiState.IN_PROGRESS)
            Log.e("LoginViewModel",email+password)
            val isLoginSuccessful=loginUseCase(email,password)
            if (isLoginSuccessful) _uiState.postValue(UiState.LOGIN_SUCCESS)
            else _uiState.postValue(UiState.LOGIN_FAILED)


        }
    }

    enum class UiState{
        IDLE,
        IN_PROGRESS,
        LOGIN_SUCCESS,
        LOGIN_FAILED
    }
}