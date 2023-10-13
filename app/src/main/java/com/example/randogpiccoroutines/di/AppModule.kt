package com.example.randogpiccoroutines.di

import com.example.randogpiccoroutines.data.network.RandomDogPicApi
import com.example.randogpiccoroutines.data.repository.RandomDogPicRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)

class AppModule {


    @Provides
    @Singleton
    fun provideApi():RandomDogPicApi {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breeds/image/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(RandomDogPicApi::class.java)
    }


}