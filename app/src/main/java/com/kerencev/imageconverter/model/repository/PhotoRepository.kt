package com.kerencev.imageconverter.model.repository

import android.content.Context
import io.reactivex.rxjava3.core.Single

interface PhotoRepository {
    fun getImagesFromGallery(context: Context): Single<List<String>>
}