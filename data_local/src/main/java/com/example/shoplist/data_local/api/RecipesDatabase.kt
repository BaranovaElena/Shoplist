package com.example.shoplist.data_local.api

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.shoplist.data_local.models.MealShortEntityResponse

@Database(entities = [MealShortEntityResponse::class], version = 1, exportSchema = false)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun getDao(): RecipesDao
}

@Dao
interface RecipesDao {
    @Query("SELECT * FROM meals")
    fun getAllMeals(): List<MealShortEntityResponse>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveMeal(entity: MealShortEntityResponse)

    @Delete
    fun deleteMeal(meal: MealShortEntityResponse)

    @Query("SELECT EXISTS(SELECT * FROM meals WHERE id = :id)")
    fun isMealExist(id : Int) : Boolean
}