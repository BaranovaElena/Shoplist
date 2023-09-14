package com.example.shoplist.data_local.api

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.shoplist.data_local.models.IngredientEntityResponse

@Database(entities = [IngredientEntityResponse::class], version = 1)
abstract class IngredientsDatabase : RoomDatabase() {
    abstract fun getDao(): IngredientsDao
}

@Dao
interface IngredientsDao {
    @Query("SELECT * FROM ingredients")
    fun getAllIngredients(): List<IngredientEntityResponse>

    @Query("SELECT * FROM ingredients WHERE title = :title LIMIT 1")
    fun getIngredientByName(title: String): IngredientEntityResponse

    @Query("SELECT EXISTS(SELECT * FROM ingredients WHERE title = :title)")
    fun isIngredientExists(title : String) : Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveIngredient(entity: IngredientEntityResponse)

    @Query("DELETE FROM ingredients WHERE title = :title")
    fun deleteIngredientByTitle(title: String)
}