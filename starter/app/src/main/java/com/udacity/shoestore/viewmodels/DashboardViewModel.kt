package com.udacity.shoestore.dashboard

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.data.models.Shoe

class DashboardViewModel : ViewModel() {

    private val _openAddItem = MutableLiveData(false)
    val openAddItem: LiveData<Boolean> get() = _openAddItem

    val nameField = ObservableField<String>()
    val companyField = ObservableField<String>()
    val sizeField = ObservableField<String>()
    val descriptionField = ObservableField<String>()

    private val _addedShoeLiveData = MutableLiveData<Boolean>()
    val addedShoeLiveData: LiveData<Boolean> get() = _addedShoeLiveData

    private val _shoeList = MutableLiveData(mutableListOf<Shoe>())
    val shoeList: LiveData<MutableList<Shoe>> get() = _shoeList

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
            _addedShoeLiveData.value = true
            clearFields()
        }

        _addedShoeLiveData.value = false
    }

    private fun clearFields() {
        nameField.set("")
        companyField.set("")
        sizeField.set("")
        descriptionField.set("")
    }


}