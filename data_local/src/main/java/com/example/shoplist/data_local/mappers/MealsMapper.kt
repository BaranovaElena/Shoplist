package com.example.shoplist.data_local.mappers

import com.example.shoplist.data_local.models.MealShortEntityResponse
import com.example.shoplist.domain.models.MealShortEntity

class MealsMapper {

    fun toDomain(response: MealShortEntityResponse) =
        MealShortEntity(
            title = response.title,
            imageUrl = response.imageUrl,
            id = response.id,
        )

    fun toApi(domain: MealShortEntity) =
        MealShortEntityResponse(
            title = domain.title,
            imageUrl = domain.imageUrl,
            id = domain.id,
        )
}
