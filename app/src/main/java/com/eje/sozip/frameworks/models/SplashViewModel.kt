package com.eje.sozip.frameworks.models

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SplashViewModel : ViewModel() {
    private val _hasSignedIn = MutableStateFlow<Boolean>(false)
    val hasSignedIn : StateFlow<Boolean> = _hasSignedIn

    fun checkStatus(){
        val auth = FirebaseAuth.getInstance()

        _hasSignedIn.value = auth.currentUser?.uid != null
    }
}