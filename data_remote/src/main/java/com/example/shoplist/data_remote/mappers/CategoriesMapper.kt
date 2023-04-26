package com.example.shoplist.data_remote.mappers

import com.example.shoplist.data_remote.models.CategoriesEntityResponse
import com.example.shoplist.domain.models.CategoriesEntity
import com.example.shoplist.domain.models.Category

class CategoriesMapper {

    fun toDomain(response: CategoriesEntityResponse) =
        CategoriesEntity(
            categories = response.categories.map { Category(it.title) }
        )
}