package com.udacity.shoestore.events

sealed class LoginEvents {

    class OpenRegisterUser : LoginEvents()

    class RegisteredUser : LoginEvents()

    class LoginUser(val token: String) : LoginEvents()

    class ErrorLogin(val message: String) : LoginEvents()

}