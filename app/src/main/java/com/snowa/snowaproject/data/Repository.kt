package com.snowa.snowaproject.data

interface Repository {
    suspend fun getCoins(): List<Coin>
}