package com.example.randogpiccoroutines.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randogpiccoroutines.data.repository.RandomDogPicRepository
import com.example.randogpiccoroutines.data.repository.RandomDogPicRepositoryImpl
import com.example.randogpiccoroutines.model.Dog
import com.example.randogpiccoroutines.util.EspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
open class RandomDogPicViewModel @Inject constructor(private val repository: RandomDogPicRepository) :
    ViewModel() {


    private val loadingLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Boolean>()
    private val dog = MutableLiveData<Dog>()

    var dispatcher = Dispatchers.IO
    fun getLoading(): LiveData<Boolean> = loadingLiveData
    fun getError(): LiveData<Boolean> = errorLiveData
    fun getDog(): LiveData<Dog> = dog


    fun initialize() {

        fetch()

    }


    @SuppressLint("SuspiciousIndentation")
    fun fetch() {
        EspressoIdlingResource.increment()
        Log.d(
            "MainActivity", "isIdleNow() ${EspressoIdlingResource.getIdlingResource()?.isIdleNow}"
        )


        initializeLiveData()
        Log.d(
            "MainActivity", "error changed"
        )
        viewModelScope.launch(dispatcher) {

/*
            val getDogPicRequest = async(dispatcher) { repository.getDog() }
            val response = getDogPicRequest.await()


 */
            val response =  repository.getDog()

            if (response.isSuccessful) {
                val dog = response.body()
                if (dog != null) onSuccess(dog)
                else {
                    onError()
                }


            } else {
                onError()
            }


        }


    }

    private fun onError() {
        loadingLiveData.postValue(false)
        errorLiveData.postValue(true)
        EspressoIdlingResource.decrement()
        Log.d(
            "MainActivity", "isIdleNow() ${EspressoIdlingResource.getIdlingResource()?.isIdleNow}"
        )
    }

    private fun onSuccess(dog: Dog) {

        loadingLiveData.postValue(false)
        errorLiveData.postValue(false)
        this@RandomDogPicViewModel.dog.postValue(dog)
        Log.d(
            "MainActivity", "error changed"
        )


    }

    fun initializeLiveData() {

        loadingLiveData.postValue(true)
        errorLiveData.postValue(false)


    }


}