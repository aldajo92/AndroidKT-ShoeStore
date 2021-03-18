package com.udacity.shoestore.repositories.shoes

import com.udacity.shoestore.data.models.Shoe

interface ShoeRepository {
    fun getItems(): List<Shoe>

    fun saveItems(shoeItems: List<Shoe>)
}