package com.snowa.snowaproject.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.snowa.snowaproject.data.local.ApiCacheDb
import com.snowa.snowaproject.data.remote.ApiInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.nio.channels.NotYetBoundException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RepositoryImplTest {

    private lateinit var repository: Repository

    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope()

    private var db = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        ApiCacheDb::class.java
    ).allowMainThreadQueries().build()

    @MockK
    private lateinit var api: ApiInterface

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = RepositoryImpl(
            api,
            db,
            dispatcher
        )
    }

    @Test
    fun `when cache is empty data must fetch from api`() {
        scope.launch {
            // Call get coins
            repository.getCoins()
            // Confirm api call called
            coVerify { api.getCoins() }
            confirmVerified(api)
        }
    }

    @Test
    fun `when cache has data must fetch from local db`() {
        scope.launch {
            // Save a coin to local db
            db.coinDao.saveCoinList(
                listOf(
                    Coin(
                        "bitcoin",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                )
            )
            // Call get coins
            repository.getCoins()
            // Confirm api call not called
            coVerify(exactly = 0) { api.getCoins() }
            confirmVerified(api)
        }
    }

    @Test
    fun `Check cached data is equal to received data`() {
        scope.launch {
            // Save a coin to local db
            db.coinDao.saveCoinList(
                listOf(
                    Coin(
                        "bitcoin",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                )
            )
            // Call get coins
            val data = repository.getCoins()
            // Check coin id
            Truth.assertThat(data[0].id).isEqualTo("bitcoin")
        }
    }
}