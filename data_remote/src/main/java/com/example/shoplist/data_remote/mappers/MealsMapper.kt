package com.example.shoplist.data_remote.mappers

import com.example.shoplist.data_remote.models.MealsEntityResponse
import com.example.shoplist.domain.models.MealShortEntity
import com.example.shoplist.domain.models.MealsEntity

class MealsMapper {

    fun toDomain(response: MealsEntityResponse) =
        MealsEntity(
            meals = response.meals?.map {
                MealShortEntity(
                    title = it.title,
                    imageUrl = it.imageUrl,
                    id = it.id,
                )
            } ?: emptyList()
        )
}