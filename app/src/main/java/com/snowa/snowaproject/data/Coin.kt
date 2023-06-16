package com.snowa.snowaproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

@Entity
data class Coin(

    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("symbol")
    val symbol: String?,

    @field:SerializedName("image")
    val image: String?,

    @field:SerializedName("last_updated")
    val lastUpdated: String?,

    @field:SerializedName("market_cap")
    val marketCap: BigDecimal?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("high_24h")
    val high24h: BigDecimal?,

    @field:SerializedName("low_24h")
    val low24h: BigDecimal?,

    @field:SerializedName("market_cap_rank")
    val marketCapRank: BigDecimal?,

    @field:SerializedName("current_price")
    val currentPrice: BigDecimal?
)
