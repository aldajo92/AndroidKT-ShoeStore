package com.udacity.shoestore.login

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.data.events.DataEvent
import com.udacity.shoestore.data.events.LoginEvents
import com.udacity.shoestore.isValidEmail
import kotlin.random.Random

class LoginViewModel : ViewModel() {

    val emailField = ObservableField<String>()
    val passwordField = ObservableField<String>()

    private val _loginLiveData = MutableLiveData(false)
    val login: LiveData<Boolean> get() = _loginLiveData

    private val _eventsLiveData = MutableLiveData<DataEvent<LoginEvents>>()
    val eventsLiveData: LiveData<DataEvent<LoginEvents>> get() = _eventsLiveData

    fun login() {
        if (validateLoginFields()) {
            _eventsLiveData.value = DataEvent(LoginEvents.LoginUser(getUserToken()))
        }
    }

    private fun validateLoginFields(): Boolean {
        val validEmail = emailField.get()?.isValidEmail() == true
        val validPassword = passwordField.get()?.isNotBlank() == true

        if (validEmail && validPassword) return true
        else if (!validEmail) {
            _eventsLiveData.value = DataEvent(LoginEvents.ErrorLogin("Invalid email format"))
        } else if (!validPassword) {
            _eventsLiveData.value = DataEvent(LoginEvents.ErrorLogin("Please enter password"))
        }

        return false
    }

    fun openRegisterUser() {
        _eventsLiveData.value = DataEvent(LoginEvents.OpenRegisterUser())
    }

    fun createNewUser() {
        _eventsLiveData.value = DataEvent(LoginEvents.RegisteredUser())
    }

    private fun getUserToken(): String = Random.nextInt(1000, 9999).toString()

}