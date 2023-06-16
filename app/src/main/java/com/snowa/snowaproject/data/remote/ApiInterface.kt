package com.snowa.snowaproject.data.remote

import com.snowa.snowaproject.data.Coin
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") currency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false
    ): List<Coin>
}