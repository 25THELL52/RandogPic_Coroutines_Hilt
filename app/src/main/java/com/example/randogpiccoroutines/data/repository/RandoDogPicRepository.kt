package com.example.randogpiccoroutines.data.repository

import com.example.randogpiccoroutines.model.Dog
import io.reactivex.Single
import retrofit2.Response

interface RandomDogPicRepository {

    suspend fun getDog(): Response<Dog>

}