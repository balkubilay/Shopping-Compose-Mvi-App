package com.example.shoppingcomposemvi.domain.model.converter

import androidx.room.TypeConverter
import com.example.shoppingcomposemvi.domain.model.Basket
import com.google.gson.Gson

class BasketConverter {
    @TypeConverter
    fun modelToJson(value: Basket?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToModel(value: String): Basket = Gson().fromJson(value, Basket::class.java)
}