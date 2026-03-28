package hu.bme.aut.android.cicanevelde.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.bme.aut.android.cicanevelde.data.converters.Converters
import hu.bme.aut.android.cicanevelde.data.dao.CatDao
import hu.bme.aut.android.cicanevelde.data.dao.GameStateDao
import hu.bme.aut.android.cicanevelde.data.dao.ItemDao
import hu.bme.aut.android.cicanevelde.data.dao.OwnedItemDao
import hu.bme.aut.android.cicanevelde.data.entity.CatEntity
import hu.bme.aut.android.cicanevelde.data.entity.GameStateEntity
import hu.bme.aut.android.cicanevelde.data.entity.ItemEntity
import hu.bme.aut.android.cicanevelde.data.entity.OwnedItemEntity

@Database(
    entities = [
        GameStateEntity::class,
        CatEntity::class,
        ItemEntity::class,
        OwnedItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameStateDao(): GameStateDao
    abstract fun catDao(): CatDao
    abstract fun itemDao(): ItemDao
    abstract fun ownedItemDao(): OwnedItemDao
}