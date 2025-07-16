package com.example.myfoodsapp.retrofit.repository

import com.example.myfoodsapp.R
import com.example.myfoodsapp.retrofit.datasource.LocalDataSource
import com.example.myfoodsapp.retrofit.datasource.RemoteDataSource
import com.example.myfoodsapp.utils.Resource
import com.example.myfoodsapp.room.entity.CardEntity
import com.example.myfoodsapp.room.entity.OrderItem
import com.example.myfoodsapp.retrofit.model.Basket
import com.example.myfoodsapp.retrofit.model.CrudResponse
import com.example.myfoodsapp.retrofit.model.Food
import com.example.myfoodsapp.retrofit.model.LanguageItem
import com.example.myfoodsapp.retrofit.model.PaymentItem
import com.example.myfoodsapp.retrofit.model.PopularRestaurants
import com.example.myfoodsapp.retrofit.model.ProfileOption

class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    /**
     * Check if a food item is favorite for the current user.
     * @param foodId id of the food
     * @param callback returns true if favorite, false otherwise
     */
    fun getCheckIfFavorite(foodId: Int, callback: (Boolean) -> Unit) {
        remoteDataSource.getCheckIfFavorite(foodId)
            .addOnSuccessListener { doc -> callback(doc.exists()) }
            .addOnFailureListener { callback(false) }
    }

    suspend fun insertOrder(orderItem: OrderItem) {
        localDataSource.insertOrder(orderItem)
    }

    suspend fun insertCard(cardEntity: CardEntity) {
        localDataSource.insertCard(cardEntity)
    }

    suspend fun getOrders(): List<OrderItem> {
        return localDataSource.getOrders()
    }

    /**
     * Toggle favorite status of a food item.
     * @param food Food item to toggle
     * @param isFavorite current favorite status
     * @param callback returns new favorite status after toggle
     */
    fun getToggleFavorite(food: Food, isFavorite: Boolean, callback: (Boolean) -> Unit) {
        remoteDataSource.getToggleFavorite(food, isFavorite)
            .addOnSuccessListener { callback(!isFavorite) }
            .addOnFailureListener { callback(isFavorite) }
    }

    fun getRemoveFromFavorites(foodId: Int) = remoteDataSource.getRemoveFromFavorites(foodId)

    fun getFavoriteFoods(onDataChanged: (List<Food>) -> Unit) =
        remoteDataSource.getFavoriteFoods(onDataChanged)

    fun logout() = remoteDataSource.logout()

    suspend fun addToCart(
        foodName: String,
        foodPhotoName: String,
        foodPrice: Int,
        foodOrderQuantity: Int,
        username: String
    ) = remoteDataSource.addToCart(foodName, foodPhotoName, foodPrice, foodOrderQuantity, username)

    suspend fun getBasket(username: String): Resource<List<Basket>> {
        return try {
            val response = remoteDataSource.getBasket(username)
            if (response.success == 1) {
                Resource.Success(response.basketList)
            } else {
                Resource.Error("API success = 0")
            }
        } catch (e: Exception) {
            Resource.Error("Network or unknown error: ${e.message}")
        }
    }

    suspend fun deleteFood(foodId: Int, username: String): Resource<CrudResponse> {
        return try {
            val response = remoteDataSource.deleteFood(foodId, username)
            if (response.success == 1) {
                Resource.Success(response)
            } else {
                Resource.Error("Failed to delete: ${response.message}")
            }
        } catch (e: Exception) {
            Resource.Error("Network or unknown error: ${e.message}")
        }
    }

    suspend fun deleteBasketItem(foodId: Int, username: String): Resource<CrudResponse> {
        return try {
            val response = remoteDataSource.deleteBasketItem(foodId, username)
            if (response.success == 1) {
                Resource.Success(response)
            } else {
                Resource.Error("Failed to delete: ${response.message}")
            }
        } catch (e: Exception) {
            Resource.Error("Network or unknown error: ${e.message}")
        }
    }

    suspend fun getCurrentFoods(): Resource<List<Food>> {
        return try {
            val response = remoteDataSource.getCurrentFoods()
            if (response.success == 1) {
                Resource.Success(response.foodList)
            } else {
                Resource.Error("API success = 0")
            }
        } catch (e: Exception) {
            Resource.Error("Network or unknown error: ${e.message}")
        }
    }

    suspend fun getSearchFood(query: String): Resource<List<Food>> {
        return try {
            val response = remoteDataSource.getCurrentFoods()
            if (response.success == 1) {
                val filteredList = response.foodList.filter {
                    it.foodName.contains(query, ignoreCase = true)
                }
                Resource.Success(filteredList)
            } else {
                Resource.Error("API success = 0")
            }
        } catch (e: Exception) {
            Resource.Error("Search failed: ${e.message}")
        }
    }

    suspend fun getPopRestaurant(): List<PopularRestaurants> = remoteDataSource.getPopRestaurant()

    /**
     * Get payment methods including saved cards.
     * Static payment methods come first, then cards from local database.
     */
    suspend fun getPayments(): List<PaymentItem> {
        val list = ArrayList<PaymentItem>()

        list.add(PaymentItem(1, R.drawable.img_paypal, "PayPal"))
        list.add(PaymentItem(2, R.drawable.img_google, "Google Pay"))
        list.add(PaymentItem(3, R.drawable.img_cash, "Cash"))

        val cards = localDataSource.getAllCards()
        cards.forEachIndexed { index, card ->
            list.add(
                PaymentItem(
                    id = 10 + index,
                    iconResId = R.drawable.img_mastercard,
                    title = "**** **** **** ${card.cardNumber.takeLast(4)}"
                )
            )
        }
        return list
    }

    // Language functions delegate to localDataSource
    fun getCurrentLanguage(): String = localDataSource.getCurrentLanguage()

    fun saveLanguage(code: String) = localDataSource.saveLanguage(code)

    fun getLanguageList(): List<LanguageItem> = localDataSource.getLanguageList()

    fun getProfileOptions(): List<ProfileOption> = localDataSource.getProfileOptions()
}
