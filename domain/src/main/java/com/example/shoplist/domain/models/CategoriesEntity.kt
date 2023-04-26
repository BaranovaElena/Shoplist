package com.example.shoplist.domain.models

data class Category(
    val title: String = ""
)

data class CategoriesEntity(
    val categories: List<Category> = emptyList()
)