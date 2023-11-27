package com.example.randogpiccoroutines

import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.randogpiccoroutines.data.repository.RandomDogPicRepositoryImpl
import com.example.randogpiccoroutines.databinding.ActivityMainBinding
import com.example.randogpiccoroutines.model.Dog
import com.example.randogpiccoroutines.util.ImageLoaderImpl
import com.example.randogpiccoroutines.viewModel.RandomDogPicViewModel
import com.example.randogpiccoroutines.viewModel.RandomDogPicViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: RandomDogPicViewModel
    @Inject
    lateinit var imageLoader: ImageLoaderImpl
    @Inject
    lateinit var repository: RandomDogPicRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(
            this,
            RandomDogPicViewModelFactory(repository)
        )[RandomDogPicViewModel::class.java]

        setUpObservers()


        binding.fetchBtn.setOnClickListener {

            viewModel.fetch()
        }
        viewModel.initialize()


    }


    fun displayDogPic(it: Dog) {

        Log.d("MainActivity", "url : ${it.message}  Thread :${Thread.currentThread().name}")


        imageLoader.load(this, it.message, binding.dogImageImgv)

        Log.d("MainActivity", "Image loaded ")

    }

    private fun showError(it: Boolean) {


        binding.errorContainer.root.visibility = if (it) View.VISIBLE else View.INVISIBLE


        Log.d(
            "MainActivity",
            "inside showError($it) errorVisibility = ${binding.errorContainer.root.isVisible}" +
                    "Thread :${Thread.currentThread().name}"
        )


    }

    private fun showLoading(it: Boolean) {


        binding.loadingContainer.root.visibility = if (it) View.VISIBLE else View.INVISIBLE

        Log.d(
            "MainActivity",
            "inside showLoading($it) errorVisibility = ${binding.errorContainer.root.isVisible}" +
                    "Thread :${Thread.currentThread().name}"
        )


    }

    fun setUpObservers() {
        viewModel.getLoading().observe(this, Observer {
            showLoading(it)

            Log.d("MainActivity", "Thread : ${Thread.currentThread().name}")

            Log.d("MainActivity", "showLoading($it)")

        })
        viewModel.getError().observe(this, Observer {
            showError(it)

            Log.d("MainActivity", "showError($it)")

        })


        viewModel.getDog().observe(this, Observer {
            displayDogPic(it)

        })
    }

    fun removeObservers() {
        viewModel.getLoading().removeObservers(this)
        Log.d("MainActivity", "getLoading() observer removed")


        viewModel.getError().removeObservers(this)
        Log.d("MainActivity", "getERROR() observer removed")


        viewModel.getDog().removeObservers(this)
        Log.d("MainActivity", "getDog() observer removed")

    }

    // 1



}