package com.example.randogpiccoroutines

import android.app.Application
import com.example.randogpiccoroutines.data.network.RandomDogPicApi
import com.example.randogpiccoroutines.data.repository.RandomDogPicRepository
import com.example.randogpiccoroutines.data.repository.RandomDogPicRepositoryImpl
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RandomDogPicApplication : Application() {


}