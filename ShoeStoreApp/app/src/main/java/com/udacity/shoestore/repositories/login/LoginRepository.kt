package com.udacity.shoestore.repositories.login

import com.udacity.shoestore.data.models.User

interface LoginRepository {

    fun authenticateUser(user: User): String?

    fun registerUser(user: User): Boolean

    fun userAuthenticated(): String?

    fun logout(): Boolean

}