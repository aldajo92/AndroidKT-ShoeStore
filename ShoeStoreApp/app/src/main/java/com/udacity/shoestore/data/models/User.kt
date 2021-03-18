package com.udacity.shoestore.data.models

data class User(val email: String, val password: String, val token: String = "")
