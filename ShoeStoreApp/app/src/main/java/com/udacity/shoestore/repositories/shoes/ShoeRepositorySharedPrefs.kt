package com.udacity.shoestore.repositories.shoes

import android.content.Context
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.udacity.shoestore.data.models.Shoe
import com.udacity.shoestore.repositories.login.LoginRepository
import java.lang.reflect.Type


class ShoeRepositorySharedPrefs(
    private val context: Context,
    private val loginRepository: LoginRepository
) : ShoeRepository {

    private val preferenceManager by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun getItems(): List<Shoe> {
        return getItemsFromSharedPrefs()
    }

    override fun saveItems(shoeItems: List<Shoe>) {
        saveItemsSharedPrefs(shoeItems)
    }

    private fun saveItemsSharedPrefs(shoeItems: List<Shoe>) {
        val token = loginRepository.userAuthenticated()
        val rawItems = Gson().toJson(shoeItems)
        preferenceManager.edit().putString(token, rawItems).apply()
    }

    private fun getItemsFromSharedPrefs(): List<Shoe> {
        val token = loginRepository.userAuthenticated()
        val rawItems = preferenceManager.getString(token, "")
        if (!rawItems.isNullOrBlank()) {
            val listShoe: Type = object : TypeToken<ArrayList<Shoe?>?>() {}.type
            return Gson().fromJson(rawItems, listShoe)
        }
        return emptyList()
    }

}