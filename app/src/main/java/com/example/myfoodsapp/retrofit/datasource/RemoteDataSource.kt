package com.example.myfoodsapp.retrofit.datasource

import com.example.myfoodsapp.R
import com.example.myfoodsapp.retrofit.apiservice.FoodsApiService
import com.example.myfoodsapp.retrofit.model.BasketResponse
import com.example.myfoodsapp.retrofit.model.CrudResponse
import com.example.myfoodsapp.retrofit.model.Food
import com.example.myfoodsapp.retrofit.model.FoodsResponse
import com.example.myfoodsapp.retrofit.model.PopularRestaurants
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.ArrayList

class RemoteDataSource(private val foodsApi: FoodsApiService) {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    suspend fun addToCart(
        foodName: String,
        foodPhotoName: String,
        foodPrice: Int,
        foodOrderQuantity: Int,
        username: String
    ): CrudResponse =
        foodsApi.addToCart(foodName, foodPhotoName, foodPrice, foodOrderQuantity, username)

    suspend fun getBasket(username: String): BasketResponse = foodsApi.getBasket(username)

    suspend fun deleteFood(foodId: Int, username: String) = foodsApi.deleteFood(foodId, username)

    suspend fun deleteBasketItem(foodId: Int, username: String) = deleteFood(foodId, username)

    fun logout() = auth.signOut()

    fun getCheckIfFavorite(foodId: Int): Task<DocumentSnapshot> {
        val userId =
            auth.currentUser?.uid ?: return Tasks.forException(Exception("User not logged in"))
        return firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .document(foodId.toString())
            .get()
    }

    fun getToggleFavorite(food: Food, isFavorite: Boolean): Task<Void> {
        return if (isFavorite) getRemoveFromFavorites(food.foodId) else getAddToFavorites(food)
    }

    fun getRemoveFromFavorites(foodId: Int): Task<Void> {
        val userId =
            auth.currentUser?.uid ?: return Tasks.forException(Exception("User not logged in"))
        return firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .document(foodId.toString())
            .delete()
    }

    private fun getAddToFavorites(food: Food): Task<Void> {
        val userId =
            auth.currentUser?.uid ?: return Tasks.forException(Exception("User not logged in"))
        val foodMap = hashMapOf(
            "foodId" to food.foodId,
            "foodName" to food.foodName,
            "foodPhotoName" to food.foodPhotoName,
            "foodPrice" to food.foodPrice
        )
        return firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .document(food.foodId.toString())
            .set(foodMap)
    }

    fun getFavoriteFoods(onDataChanged: (List<Food>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val foodList = snapshot.documents.mapNotNull { it.toObject(Food::class.java) }
                onDataChanged(foodList)
            }
    }

    suspend fun getCurrentFoods(): FoodsResponse = foodsApi.getCurrentFoods()

    private val popularRestaurants = listOf(
        PopularRestaurants(R.drawable.img_mcdonalds, "McDonald's"),
        PopularRestaurants(R.drawable.img_kfc, "KFC"),
        PopularRestaurants(R.drawable.img_burgerking, "Burger King"),
        PopularRestaurants(R.drawable.img_dominos, "Dominos"),
        PopularRestaurants(R.drawable.img_papajohns, "Papa Johns"),
        PopularRestaurants(R.drawable.img_starbucks, "Starbucks"),
        PopularRestaurants(R.drawable.img_wendys, "Wendy's"),
        PopularRestaurants(R.drawable.img_pizzahut, "Pizza Hut"),
        PopularRestaurants(R.drawable.img_subway, "Subway"),
        PopularRestaurants(R.drawable.img_nusret, "Nusr-Et"),
        PopularRestaurants(R.drawable.img_mado, "Mado"),
        PopularRestaurants(R.drawable.img_shaurma, "Shaurma N1"),
        PopularRestaurants(R.drawable.img_pizza, "Pizza-Mizza"),
        PopularRestaurants(R.drawable.img_coffe, "Costa Coffee")
    )

    suspend fun getPopRestaurant(): List<PopularRestaurants> = withContext(Dispatchers.IO) {
        popularRestaurants
    }
}
