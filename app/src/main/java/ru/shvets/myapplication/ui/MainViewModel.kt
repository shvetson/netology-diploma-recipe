package ru.shvets.myapplication.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.shvets.myapplication.App
import ru.shvets.myapplication.model.Recipe
import ru.shvets.myapplication.model.RecipeCategory
import ru.shvets.myapplication.model.Step
import ru.shvets.myapplication.utils.Constants

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val recipeRepository = (application.applicationContext as App).recipeRepository
    private val stepRepository = (application.applicationContext as App).stepRepository


    val data get() = recipeRepository.getAll

    fun getAllRecipes(): LiveData<List<RecipeCategory>> {
        return recipeRepository.getAllRecipes()
    }

    fun getRecipe(id: Long) : Recipe {
        return recipeRepository.getById(id)
    }

    fun getFavorites(): LiveData<List<RecipeCategory>> {
        return recipeRepository.getFavorites()
    }

    fun search(searchQuery: String): List<RecipeCategory> {
        return recipeRepository.search(searchQuery)
    }

    fun save(recipe: Recipe):Long {
           return recipeRepository.save(recipe)
    }

    fun delete(recipe: RecipeCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.delete(recipe.id)
        }
    }

    fun updateLiked(recipe: RecipeCategory) {
            recipeRepository.updateLiked(recipe)
    }

    fun updateDragDrop(recipeStart: Recipe, recipeEnd: Recipe) {
        val startSortId = recipeStart.sortId
        val endSortId = recipeEnd.sortId

        recipeRepository.updateSortId(sortId = 99999, id = recipeStart.id)
        recipeRepository.updateSortId(sortId = startSortId, id = recipeEnd.id)
        recipeRepository.updateSortId(sortId = endSortId, id = recipeStart.id)
    }

    fun insertAll(list: List<Step>, recipeId: Long) {
        val updatedList = list.map {step->
            step.copy(
                id = step.id,
                recipeId = recipeId,
                orderId = list.indexOf(step).toLong() + 1
            )
        }
        stepRepository.deleteAll(recipeId)
        stepRepository.insertAll(updatedList)
    }
}