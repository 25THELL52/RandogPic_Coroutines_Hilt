package com.example.randogpiccoroutines.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.randogpiccoroutines.data.repository.RandomDogPicRepository
import com.example.randogpiccoroutines.data.repository.RandomDogPicRepositoryImpl

class RandomDogPicViewModelFactory (private val repository: RandomDogPicRepository)

    : ViewModelProvider.Factory {


        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RandomDogPicViewModel(repository) as T
        }


 
}






