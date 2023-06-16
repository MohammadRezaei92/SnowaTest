package com.snowa.snowaproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.snowa.snowaproject.data.Coin

@Dao
interface CoinDao {
    @Insert(onConflict = REPLACE)
    suspend fun saveCoinList(coins: List<Coin>)

    @Query("SELECT * FROM coin")
    suspend fun getCoinList(): List<Coin>
}