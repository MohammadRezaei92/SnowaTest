package com.snowa.snowaproject.data

import com.snowa.snowaproject.data.local.ApiCacheDb
import com.snowa.snowaproject.data.remote.ApiInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    private val apiCacheDb: ApiCacheDb,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : Repository {

    override suspend fun getCoins(): List<Coin> = withContext(dispatcher) {
        var coins = getCoinsFromCache()
        if (coins.isEmpty()) {
            coins = fetchCoinsFromApi()
            saveCoins(coins)
        }
        return@withContext coins
    }

    private suspend fun getCoinsFromCache(): List<Coin> {
        return apiCacheDb.coinDao.getCoinList()
    }

    private suspend fun saveCoins(coinList: List<Coin>) {
        apiCacheDb.coinDao.saveCoinList(coinList)
    }

    private suspend fun fetchCoinsFromApi(): List<Coin> {
        return apiInterface.getCoins()
    }
}