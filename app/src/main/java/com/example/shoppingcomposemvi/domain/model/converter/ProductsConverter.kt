package com.example.shoppingcomposemvi.domain.model.converter

import androidx.room.TypeConverter
import com.example.shoppingcomposemvi.domain.model.Product
import com.google.gson.Gson

class ProductsConverter {
    @TypeConverter
    fun listToJson(value: List<Product>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Product>::class.java).toList()

}