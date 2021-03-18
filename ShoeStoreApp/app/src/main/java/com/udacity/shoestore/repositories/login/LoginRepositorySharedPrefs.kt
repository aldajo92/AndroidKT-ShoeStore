package com.udacity.shoestore.repositories.login

import android.content.Context
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.udacity.shoestore.data.models.User
import kotlin.random.Random

class LoginRepositorySharedPrefs(private val context: Context) : LoginRepository {

    companion object {
        const val AUTHENTICATED = "AUTHENTICATED_USER"
        const val USER_PREFIX = "user"
    }

    private val preferenceManager by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun authenticateUser(user: User): String? {
        return checkUserAndReturnWithToken(user)?.let {
            storeAuthenticatedUser(it)
            it.token
        }
    }

    override fun registerUser(user: User): Boolean {
        return storeNewUser(user)
    }

    override fun userAuthenticated(): String? {
        return getStoredUser()?.let {
            checkUserAndReturnWithToken(it)?.token
        }
    }

    override fun logout(): Boolean {
        removeAuthenticatedUser()
        return true
    }

    private fun getStoredUser(): User? {
        val userRaw = preferenceManager.getString(AUTHENTICATED, "")
        if (!userRaw.isNullOrBlank()) {
            return Gson().fromJson(userRaw, User::class.java)
        }
        return null
    }

    private fun storeAuthenticatedUser(user: User) {
        val userJson = Gson().toJson(user)
        preferenceManager.edit().putString(AUTHENTICATED, userJson).apply()
    }

    private fun removeAuthenticatedUser() {
        preferenceManager.edit().putString(AUTHENTICATED, "").apply()
    }

    private fun storeNewUser(user: User): Boolean {
        if (!preferenceManager.contains("${USER_PREFIX}_${user.email}")) {
            val token = generateToken()
            val userWithToken = user.copy(token = token)
            val userJson = Gson().toJson(userWithToken)
            preferenceManager.edit().putString("${USER_PREFIX}_${userWithToken.email}", userJson)
                .apply()
            return true
        }
        return false
    }

    private fun checkUserAndReturnWithToken(user: User): User? {
        if (preferenceManager.contains("${USER_PREFIX}_${user.email}")) {
            val userRaw = preferenceManager.getString("${USER_PREFIX}_${user.email}", "")
            val storedUser = Gson().fromJson(userRaw, User::class.java)
            if (storedUser.password == user.password) return storedUser
        }
        return null
    }

    private fun generateToken(): String {
        val tokenNumber = Random.nextInt(100000, 999999)
        return tokenNumber.toString()
    }
}