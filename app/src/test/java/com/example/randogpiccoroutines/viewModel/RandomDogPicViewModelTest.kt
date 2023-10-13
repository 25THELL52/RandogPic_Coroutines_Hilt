package com.example.randogpiccoroutines.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ActivityScenario.launch
import com.example.randogpiccoroutines.data.repository.RandomDogPicRepository
import com.example.randogpiccoroutines.data.repository.RandomDogPicRepositoryImpl
import com.example.randogpiccoroutines.model.Dog
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import retrofit2.Response

val DOG = Dog("https://images.dog.ceo/breeds/terrier-american/n02093428_3556.jpg", "success")

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi

class RandomDogPicViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()


    @Mock
    lateinit var randomDogPicRepository: RandomDogPicRepository
    lateinit var randomDogPicViewModel: RandomDogPicViewModel
    lateinit var testCoroutineScope: TestScope


    @Mock
    lateinit var loadingObserver: Observer<Boolean>

    @Mock
    lateinit var errorObserver: Observer<Boolean>

    @Mock
    lateinit var dogObserver: Observer<Dog>


    @Before
    fun setUp() {

        randomDogPicViewModel = RandomDogPicViewModel(randomDogPicRepository)
        randomDogPicViewModel.dispatcher= UnconfinedTestDispatcher()
        randomDogPicViewModel.let {
            it.getDog().observeForever(dogObserver)
            it.getLoading().observeForever(loadingObserver)
            it.getError().observeForever(errorObserver)


        }

    }


    private fun setUpRepositoryWithSuccess() {

        runTest {
            val dogResponse = Response.success(DOG)
            whenever(randomDogPicRepository.getDog()).thenReturn(dogResponse)
        }

    }

    private fun setUpRepositoryWithFailure() {

        runTest {
            val dogResponse: Response<Dog> = Response.error(
                400,
                ResponseBody.create(
                    MediaType.parse("application/json"),
                    "{\"key\":[\"somestuff\"]}"
                )
            )
            whenever(randomDogPicRepository.getDog()).thenReturn(dogResponse)
        }

    }


    @Test
    fun whenInitialize_fetchIsCalled() {

        setUpRepositoryWithSuccess()
        val spyViewModel = spy(randomDogPicViewModel)
        spyViewModel.initialize()
        verify(spyViewModel).fetch()

    }

    @Test
    fun whenFetch_initializeDataIsCalled() {

        setUpRepositoryWithSuccess()
        val spyViewModel = spy(randomDogPicViewModel)
        spyViewModel.fetch()
        verify(spyViewModel).initializeLiveData()
    }


    @Test
    fun whenInitializeLiveData_loadingIsTrue() {

        randomDogPicViewModel.initializeLiveData()
        verify(loadingObserver).onChanged(true)
    }

    @Test
    fun whenInitializeLiveData_ErrorIsFalse() {

        randomDogPicViewModel.initializeLiveData()
        verify(errorObserver).onChanged(false)
    }

    @Test
    fun whenFetch_getDogIsCalled() {
        setUpRepositoryWithSuccess()
        randomDogPicViewModel.fetch()
        runTest {
            verify(randomDogPicRepository).getDog()
        }
    }


    @Test
    fun whenRepositoryReturnsSuccess_loadingIsFalse() {


            setUpRepositoryWithSuccess()
            randomDogPicViewModel.fetch()
            verify(loadingObserver).onChanged(false)



    }

    @Test
    fun whenRepositoryReturnsSuccess_errorIsFalse() {
        setUpRepositoryWithSuccess()

        randomDogPicViewModel.fetch()
        verify(errorObserver, times(2)).onChanged(false)

    }

    @Test
    fun whenRepositoryReturnsSuccess_DogValueIsChangedToDOG() {

            setUpRepositoryWithSuccess()
            randomDogPicViewModel.fetch()

        verify(dogObserver).onChanged(DOG)
    }

    @Test
    fun whenRepositoryReturnsError_loadingIsFalse() {
            setUpRepositoryWithFailure()
            randomDogPicViewModel.fetch()


        verify(loadingObserver).onChanged(false)

    }

    @Test
    fun whenRepositoryReturnsError_errorIsTrue() {
        setUpRepositoryWithFailure()

            randomDogPicViewModel.fetch()




        verify(errorObserver).onChanged(true)
    }


}