package com.example.randogpiccoroutines.data.repository

import com.example.randogpiccoroutines.model.Dog


interface RepositoryCallback  {
     fun onSuccess(dog : Dog) {
// TODO
    }
     fun onError(e: String) {
// TODO
    }
}
