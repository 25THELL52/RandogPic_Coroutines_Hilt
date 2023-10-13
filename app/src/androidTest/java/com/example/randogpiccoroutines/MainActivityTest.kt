package com.example.randogpiccoroutines

import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.randogpiccoroutines.data.repository.RandomDogPicRepository
import com.example.randogpiccoroutines.data.repository.RandomDogPicRepositoryImpl
import com.example.randogpiccoroutines.model.Dog
import com.example.randogpiccoroutines.util.EspressoIdlingResource
import com.example.randogpiccoroutines.util.ImageLoaderImpl
import com.example.randogpiccoroutines.viewModel.RandomDogPicViewModel
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.util.concurrent.TimeUnit


val DOG = Dog("https://images.dog.ceo/breeds/terrier-american/n02093428_3556.jpg", "success")

@LargeTest

@RunWith(AndroidJUnit4::class)
class MainActivityTest {


    // @get:Rule
    // var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModelSpy: RandomDogPicViewModel

    private lateinit var repository: RandomDogPicRepositoryImpl
    private lateinit var imageLoaderSpy: ImageLoaderImpl


    private lateinit var scenario: ActivityScenario<MainActivity>


    private fun setUpRepoWithSuccess() {

        runTest {
            val dogResponse = Response.success(Dog(DOG.message, DOG.status))
            whenever(repository.getDog()).thenReturn(dogResponse)
        }


    }

    private fun setUpRepoWithFailure() {

        runTest {
            val dogResponse: Response<Dog> = Response.error(
                400,
                ResponseBody.create(
                    MediaType.parse("application/json"),
                    "{\"key\":[\"somestuff\"]}")
            )
            whenever(repository.getDog()).thenReturn(dogResponse)
        }


    }

    private fun setMainActivityProperties() {


        scenario.onActivity {


            //it.removeObservers()
            it.viewModel = viewModelSpy
            it.setUpObservers()
            imageLoaderSpy = spy(ImageLoaderImpl())
            it.imageLoader = imageLoaderSpy


        }


    }

    private fun removeObservers() {
        scenario.onActivity {
            it.removeObservers()
            Log.d("MainActivity", "Observers removed :${Thread.currentThread().name}")

        }
    }


    @Before
    fun setUp() {

        repository = mock<RandomDogPicRepositoryImpl>()
        viewModelSpy = spy(RandomDogPicViewModel(repository))
        scenario = ActivityScenario.launch(MainActivity::class.java)

        //IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource())

        Espresso.registerIdlingResources(EspressoIdlingResource.getIdlingResource())
        Espresso.onIdle()


    }

    @After
    fun tearDown() {
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource())
        //IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource())
        IdlingPolicies.setIdlingResourceTimeout(3, TimeUnit.MINUTES)
        scenario.close()
    }


    @Test
    fun whenMainActivityLaunchedFetchButtonIsDisplayed() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.fetch_btn)).check(matches(isDisplayed()))
    }


    @Test
    fun whenFetchButtonIsClickedAndImageLoadingIsSuccessfulImageUrlIsCalledByImageLoader() {

        setUpRepoWithSuccess()
        setMainActivityProperties()
        onView(withId(R.id.fetch_btn)).perform(click())

        removeObservers()
        scenario.onActivity {
            Log.d("MainActivityTest:", "${viewModelSpy.getDog().value?.message} ")
            verify(it.imageLoader, times(1)).load(
                any(),
                eq(DOG.message),
                any()
            )


        }

    }

    @Test
    fun whenFetchButtonIsClickedAndImageLoadingHasFailedErrorContainerIsDisplayed() {


        setUpRepoWithFailure()


        //Thread.sleep(5000)
        setMainActivityProperties()
        Log.d("MainActivity", "BUTTON CLICKED Thread :${Thread.currentThread().name}")

        //Espresso.onIdle()
        onView(withId(R.id.fetch_btn)).perform(click())


        removeObservers()
        onView(withId(R.id.errorContainer)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

    }


    @Test
    fun whenFetchButtonIsClickedDogAndImageLoadingIsSuccessfulErrorContainerIsNotDisplayed() {

        setUpRepoWithSuccess()
        setMainActivityProperties()
        onView(withId(R.id.fetch_btn)).perform(click())
        removeObservers()
        onView(withId(R.id.errorContainer)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))


    }

    @Test
    fun whenFetchButtonIsClickedDogAndImageLoadingIsSuccessfulLoadingContainerIsNotDisplayed() {

        setUpRepoWithSuccess()
        setMainActivityProperties()
        onView(withId(R.id.fetch_btn)).perform(click())
        removeObservers()
        onView(withId(R.id.loadingContainer)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))


    }

}

