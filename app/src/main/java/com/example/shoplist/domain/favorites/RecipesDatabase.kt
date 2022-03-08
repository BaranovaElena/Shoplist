package com.example.shoplist.domain.favorites

import androidx.room.*
import com.example.shoplist.data.MealShortEntity

@Database(entities = [MealShortEntity::class], version = 1)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun getDao(): RecipesDao
}

@Dao
interface RecipesDao {
    @Query("SELECT * FROM meals")
    fun getAllMeals(): List<MealShortEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveMeal(entity: MealShortEntity)
}