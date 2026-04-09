package hu.bme.aut.android.cicanevelde.data.converters

import androidx.room.TypeConverter
import hu.bme.aut.android.cicanevelde.domain.model.enums.Gender
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemType
import hu.bme.aut.android.cicanevelde.domain.model.enums.Pattern
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType

class Converters {
    @TypeConverter
    fun fromGender(value: Gender): String = value.name

    @TypeConverter
    fun toGender(value: String): Gender = Gender.valueOf(value)

    @TypeConverter
    fun fromPattern(value: Pattern): String = value.name

    @TypeConverter
    fun toPattern(value: String): Pattern = Pattern.valueOf(value)

    @TypeConverter
    fun fromItemCode(value: ItemCode): String = value.name

    @TypeConverter
    fun toItemCode(value: String): ItemCode = ItemCode.valueOf(value)

    @TypeConverter
    fun fromItemType(value: ItemType): String = value.name

    @TypeConverter
    fun toItemType(value: String): ItemType = ItemType.valueOf(value)

    @TypeConverter
    fun fromRoomType(value: RoomType): String = value.name

    @TypeConverter
    fun toRoomType(value: String): RoomType = RoomType.valueOf(value)
}