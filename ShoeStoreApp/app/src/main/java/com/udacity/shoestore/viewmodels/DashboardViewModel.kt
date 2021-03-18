package com.udacity.shoestore.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.shoestore.data.events.DataEvent
import com.udacity.shoestore.data.models.Shoe
import com.udacity.shoestore.repositories.login.LoginRepository
import com.udacity.shoestore.repositories.login.LoginRepositorySharedPrefs
import com.udacity.shoestore.repositories.shoes.ShoeRepository
import com.udacity.shoestore.repositories.shoes.ShoeRepositorySharedPrefs

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val loginRepository: LoginRepository =
        LoginRepositorySharedPrefs(application.baseContext)

    private val shoeRepository: ShoeRepository =
        ShoeRepositorySharedPrefs(application.baseContext, loginRepository)

    private val _openAddItem = MutableLiveData(false)
    val openAddItem: LiveData<Boolean> get() = _openAddItem

    val nameField = ObservableField<String>()
    val companyField = ObservableField<String>()
    val sizeField = ObservableField<String>()
    val descriptionField = ObservableField<String>()

    private val _addedShoeLiveData = MutableLiveData<DataEvent<Boolean>>()
    val addedShoeLiveData: LiveData<DataEvent<Boolean>> get() = _addedShoeLiveData

    private val _shoeList = MutableLiveData(mutableListOf<Shoe>())
    val shoeList: LiveData<MutableList<Shoe>> get() = _shoeList

    fun getShoeItems() {
        _shoeList.value = shoeRepository.getItems().toMutableList()
    }

    fun openAddItem() {
        _openAddItem.value = true
        _openAddItem.value = false
    }

    fun saveItem() {
        val nameNotEmpty = nameField.get()?.isNotBlank() == true
        val companyNotEmpty = companyField.get()?.isNotBlank() == true
        val sizeNotEmpty = sizeField.get()?.isNotBlank() == true

        if (nameNotEmpty && companyNotEmpty && sizeNotEmpty) {
            val shoe = Shoe(
                nameField.get() ?: "",
                sizeField.get()?.toDouble() ?: 0.0,
                companyField.get() ?: "",
                descriptionField.get() ?: ""
            )
            _shoeList.value?.add(shoe)
            _addedShoeLiveData.value = DataEvent(true)

            clearDetailFields()
            shoeRepository.saveItems(_shoeList.value ?: emptyList())
        }
    }

    private fun clearDetailFields() {
        nameField.set("")
        companyField.set("")
        sizeField.set("")
        descriptionField.set("")
    }

    fun logout() {
        loginRepository.logout()
    }


}