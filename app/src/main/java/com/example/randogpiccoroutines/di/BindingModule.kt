package com.example.randogpiccoroutines.di

import com.example.randogpiccoroutines.data.repository.RandomDogPicRepository
import com.example.randogpiccoroutines.data.repository.RandomDogPicRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class BindingModule {

    @Binds
    abstract fun bindRandomDogPicRepository(randomDogPicRepositoryImpl: RandomDogPicRepositoryImpl):RandomDogPicRepository

}