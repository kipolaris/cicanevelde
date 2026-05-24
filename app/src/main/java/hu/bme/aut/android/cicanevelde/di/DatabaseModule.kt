package hu.bme.aut.android.cicanevelde.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.cicanevelde.data.AppDatabase
import hu.bme.aut.android.cicanevelde.data.dao.BowlStateDao
import hu.bme.aut.android.cicanevelde.data.dao.CatDao
import hu.bme.aut.android.cicanevelde.data.dao.GameStateDao
import hu.bme.aut.android.cicanevelde.data.dao.ItemDao
import hu.bme.aut.android.cicanevelde.data.dao.LitterStateDao
import hu.bme.aut.android.cicanevelde.data.dao.OwnedItemDao
import hu.bme.aut.android.cicanevelde.data.dao.PlacedItemDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cicanevelde_database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideCatDao(database: AppDatabase): CatDao {
        return database.catDao()
    }

    @Provides
    fun provideGameStateDao(database: AppDatabase): GameStateDao {
        return database.gameStateDao()
    }

    @Provides
    fun provideItemDao(database: AppDatabase): ItemDao {
        return database.itemDao()
    }

    @Provides
    fun providePlacedItemDao(database: AppDatabase): PlacedItemDao {
        return database.placedItemDao()
    }

    @Provides
    fun provideBowlStateDao(database: AppDatabase): BowlStateDao {
        return database.bowlStateDao()
    }

    @Provides
    fun provideLitterStateDao(database: AppDatabase): LitterStateDao {
        return database.litterStateDao()
    }

    @Provides
    fun provideOwnedItemDao(database: AppDatabase): OwnedItemDao {
        return database.ownedItemDao()
    }
}