package com.example.randogpiccoroutines.data.repository

import com.example.randogpiccoroutines.data.network.RandomDogPicApi
import com.example.randogpiccoroutines.model.Dog
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Inject


open class RandomDogPicRepositoryImpl @Inject constructor(private val api: RandomDogPicApi) : RandomDogPicRepository {


    override suspend fun getDog(): Response<Dog> {

//        Log.d("RandomDogPicRepoImpl", "sending a request to the url Thread :${Thread.currentThread().name}")
        return api.getDog()


    }


        }



