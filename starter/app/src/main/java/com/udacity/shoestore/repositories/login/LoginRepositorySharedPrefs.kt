package com.udacity.shoestore.repositories

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
        return if (checkUser(user)) {
            storeAuthenticatedUser(user)
            Random.nextInt(1000, 9999).toString()
        } else null
    }

    override fun registerUser(user: User): Boolean {
        return storeNewUser(user)
    }

    override fun userAuthenticated(): Boolean {
        return getStoredUser()?.let {
            checkUser(it)
        } ?: run {
            false
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
            val userJson = Gson().toJson(user)
            preferenceManager.edit().putString("${USER_PREFIX}_${user.email}", userJson).apply()
            return true
        }
        return false
    }

    private fun checkUser(user: User): Boolean {
        if (preferenceManager.contains("${USER_PREFIX}_${user.email}")) {
            val userRaw = preferenceManager.getString("${USER_PREFIX}_${user.email}", "")
            val localUser = Gson().fromJson(userRaw, User::class.java)
            return localUser.password == user.password
        }
        return false
    }
}