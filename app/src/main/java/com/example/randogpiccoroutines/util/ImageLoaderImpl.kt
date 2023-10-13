package com.example.randogpiccoroutines.util

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.compose.ui.graphics.drawscope.Stroke
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import javax.inject.Inject

open class ImageLoaderImpl @Inject constructor():ImageLoader {
    override fun load(context: Context, url: String, imageView: ImageView) {

        Glide.with(context).load(url).transform(RoundedCorners(20))

        .into(imageView)
        EspressoIdlingResource.decrement()
        Log.d("MainActivity","isIdleNow2() ${EspressoIdlingResource.getIdlingResource()?.isIdleNow}")

    }

}