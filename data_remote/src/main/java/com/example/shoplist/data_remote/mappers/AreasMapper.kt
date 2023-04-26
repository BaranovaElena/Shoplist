package com.example.shoplist.data_remote.mappers

import com.example.shoplist.data_remote.models.AreasEntityResponse
import com.example.shoplist.domain.models.Area
import com.example.shoplist.domain.models.AreasEntity

class AreasMapper {

    fun toDomain(response: AreasEntityResponse) =
        AreasEntity(
            areas = response.areas.map { Area(it.title) }
        )
}