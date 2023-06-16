package com.snowa.snowaproject.di

import android.content.Context
import androidx.room.Room
import androidx.viewbinding.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.snowa.snowaproject.BASE_URL
import com.snowa.snowaproject.data.Repository
import com.snowa.snowaproject.data.RepositoryImpl
import com.snowa.snowaproject.data.local.ApiCacheDb
import com.snowa.snowaproject.data.remote.ApiInterface
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttp(): OkHttpClient =
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.build()

    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient): ApiInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(ApiInterface::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object LocalDBModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ApiCacheDb {
        return Room.databaseBuilder(
            context,
            ApiCacheDb::class.java, "snowa_db"
        ).build()
    }
}

@InstallIn(SingletonComponent::class)
@Module
object CoroutineDispatcherModule {

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(
        repositoryImpl: RepositoryImpl
    ): Repository
}