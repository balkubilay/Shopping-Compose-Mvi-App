package com.example.shoppingcomposemvi.data.di

import android.content.Context
import androidx.room.Room
import com.example.shoppingcomposemvi.data.local.RoomDB
import com.example.shoppingcomposemvi.data.local.dao.BasketDao
import com.example.shoppingcomposemvi.data.local.dao.ProductDao
import com.example.shoppingcomposemvi.data.local.dao.TransactionsDao
import com.example.shoppingcomposemvi.data.local.repository.LocalRepositoryImpl
import com.example.shoppingcomposemvi.domain.repository.LocalRepository
import com.example.shoppingcomposemvi.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideRoomDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        RoomDB::class.java,
        Constants.ROOM_DB_NAME
    ).build()

    @Singleton
    @Provides
    fun provideProductDao(roomDB: RoomDB) = roomDB.productDao

    @Singleton
    @Provides
    fun provideBasketDao(roomDB: RoomDB) = roomDB.basketDao

    @Singleton
    @Provides
    fun provideTransactionsDao(roomDB: RoomDB) = roomDB.transactionsDao

    @Singleton
    @Provides
    fun provideRoomRepository(productDao: ProductDao, basketDao: BasketDao, transactionsDao: TransactionsDao): LocalRepository = LocalRepositoryImpl(productDao = productDao, basketDao = basketDao, transactionsDao = transactionsDao)

    @DefaultDispatcher
    @Provides
    internal fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    internal fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    internal fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher