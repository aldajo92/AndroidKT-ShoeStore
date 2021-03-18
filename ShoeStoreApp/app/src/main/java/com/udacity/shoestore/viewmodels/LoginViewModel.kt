package com.udacity.shoestore.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.shoestore.data.events.DataEvent
import com.udacity.shoestore.data.events.LoginEvents
import com.udacity.shoestore.data.models.User
import com.udacity.shoestore.isValidEmail
import com.udacity.shoestore.repositories.login.LoginRepository
import com.udacity.shoestore.repositories.login.LoginRepositorySharedPrefs

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val loginRepository: LoginRepository =
        LoginRepositorySharedPrefs(application.baseContext)

    val emailField = ObservableField<String>()
    val passwordField = ObservableField<String>()

    private val _loginLiveData = MutableLiveData(false)
    val login: LiveData<Boolean> get() = _loginLiveData

    private val _eventsLiveData = MutableLiveData<DataEvent<LoginEvents>>()
    val eventsLiveData: LiveData<DataEvent<LoginEvents>> get() = _eventsLiveData

    fun isUserAuthenticated(): Boolean {
        return loginRepository.userAuthenticated()?.isNotBlank() == true
    }

    fun login() {
        if (validateLoginFields()) {
            val user = getUserFromFields()
            loginRepository.authenticateUser(user)?.let {
                _eventsLiveData.value = DataEvent(LoginEvents.LoginUser(it))
            } ?: run {
                _eventsLiveData.value = DataEvent(LoginEvents.ErrorLogin("Authentication Error"))
            }
        }
    }

    fun logout() {
        loginRepository.logout()
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

    fun clearFields(){
        emailField.set("")
        passwordField.set("")
    }

    fun openRegisterUser() {
        _eventsLiveData.value = DataEvent(LoginEvents.OpenRegisterUser())
    }

    fun createNewUser() {
        if (validateLoginFields()) {
            val user = getUserFromFields()
            if (loginRepository.registerUser(user)) {
                _eventsLiveData.value = DataEvent(LoginEvents.RegisteredUser())
                _eventsLiveData.value =
                    DataEvent(LoginEvents.Message("New user registered successfully"))
            } else {
                _eventsLiveData.value = DataEvent(LoginEvents.ErrorLogin("Registering Error"))
            }
        }
    }

    private fun getUserFromFields() = User(emailField.get() ?: "", passwordField.get() ?: "")

}