package com.example.randogpiccoroutines.data.network

import com.example.randogpiccoroutines.model.Dog
import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RandomDogPicApi {


    @GET("random")
    suspend fun getDog():Response<Dog>

/*
    companion object Factory {
        fun create(): RandomDogPicApi {

            val retrofit = Retrofit.Builder()
                .baseUrl("https://dog.ceo/api/breeds/image/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RandomDogPicApi::class.java)
        }
    }

 */
}