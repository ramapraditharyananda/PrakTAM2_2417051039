package com.example.praktam2_2417051039.viewmodel

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private val _name: MutableState<String> = mutableStateOf("")
    private val _email: MutableState<String> = mutableStateOf("")
    private val _photoUrl: MutableState<String> = mutableStateOf("")
    private val _password: MutableState<String> = mutableStateOf("")
    private val _imageVersion: MutableIntState = mutableIntStateOf(0)

    val name: String get() = _name.value
    val email: String get() = _email.value
    val photoUrl: String get() = _photoUrl.value
    val password: String get() = _password.value
    val imageVersion: Int get() = _imageVersion.intValue

    fun loadFromAccount(accountName: String, accountEmail: String, accountPassword: String) {
        _name.value = accountName
        _email.value = accountEmail
        _password.value = accountPassword
        _photoUrl.value = ""
        _imageVersion.intValue = 0
    }

    fun updateProfile(newName: String, newEmail: String, newPhotoUrl: String, newPassword: String) {
        _name.value = newName
        _email.value = newEmail
        if (newPhotoUrl != _photoUrl.value) {
            _photoUrl.value = newPhotoUrl
            _imageVersion.intValue++
        }
        if (newPassword.isNotBlank()) _password.value = newPassword
    }
}