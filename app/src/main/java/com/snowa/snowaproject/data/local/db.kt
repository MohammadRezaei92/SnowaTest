package com.snowa.snowaproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.snowa.snowaproject.data.Coin
import java.math.BigDecimal

@Database(entities = [Coin::class], version = 1)
@TypeConverters(Converters::class)
abstract class ApiCacheDb : RoomDatabase() {
    abstract val coinDao: CoinDao
}

class Converters {
    @TypeConverter
    fun fromStringToBigDecimal(value: String?): BigDecimal? =
        if (value == null) null else BigDecimal(value)

    @TypeConverter
    fun fromBigDecimalToString(bigDecimal: BigDecimal?): String? = bigDecimal?.toString()
}