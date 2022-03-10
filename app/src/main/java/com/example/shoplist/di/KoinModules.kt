package com.example.shoplist.di

import androidx.room.Room
import com.example.shoplist.BuildConfig
import com.example.shoplist.domain.LoadingMealRepo
import com.example.shoplist.domain.LoadingMealRetrofitImpl
import com.example.shoplist.domain.MealRetrofitService
import com.example.shoplist.domain.favorites.*
import com.example.shoplist.viewmodel.favorites.FavoritesController
import com.example.shoplist.viewmodel.favorites.FavoritesViewModel
import com.example.shoplist.viewmodel.mealitem.MealItemController
import com.example.shoplist.viewmodel.mealitem.MealItemPresenter
import com.example.shoplist.viewmodel.recipe.RecipeFilerViewModel
import com.example.shoplist.viewmodel.recipe.RecipeFilterController
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
    single<MealRetrofitService> { get<Retrofit>().create(MealRetrofitService::class.java) }
    single<LoadingMealRepo> { LoadingMealRetrofitImpl(get<MealRetrofitService>()) }
}

val roomModule = module {
    single<RecipesDatabase> {
        Room.databaseBuilder(get(),RecipesDatabase::class.java, "recipes.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single<RecipesDao> { get<RecipesDatabase>().getDao() }
    single<SavingFavoriteMealRepo> { FavoriteMealRoomImpl(get<RecipesDao>()) }
    single<LoadingFavoritesMealRepo> { FavoriteMealRoomImpl(get<RecipesDao>()) }
}

val viewModelModule = module {
    factory<RecipeFilterController.BaseViewModel> { RecipeFilerViewModel(get<LoadingMealRepo>()) }
    factory<FavoritesController.BaseViewModel> { FavoritesViewModel(get<LoadingFavoritesMealRepo>()) }
    factory<MealItemController.Presenter> {
        MealItemPresenter(get<SavingFavoriteMealRepo>(), get<LoadingFavoritesMealRepo>())
    }
}