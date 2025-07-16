package com.example.myfoodsapp.di

import android.content.Context
import androidx.room.Room
import com.example.myfoodsapp.room.dao.OrderDao
import com.example.myfoodsapp.room.database.AppDatabase
import com.example.myfoodsapp.room.dao.CardDao
import com.example.myfoodsapp.retrofit.datasource.RemoteDataSource
import com.example.myfoodsapp.retrofit.apiservice.FoodsApiService
import com.example.myfoodsapp.retrofit.datasource.LocalDataSource
import com.example.myfoodsapp.retrofit.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://kasimadalan.pe.hu/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesApiService(retrofit: Retrofit): FoodsApiService {
        return retrofit.create(FoodsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(foodsApi: FoodsApiService): RemoteDataSource {
        return RemoteDataSource(foodsApi)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        @ApplicationContext context: Context,
        orderDao: OrderDao,
        cardDao: CardDao
    ): LocalDataSource {
        return LocalDataSource(context, orderDao, cardDao)
    }

    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): Repository {
        return Repository(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "order_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao()


    @Provides
    fun provideCardDao(db: AppDatabase): CardDao = db.cardDao()

}