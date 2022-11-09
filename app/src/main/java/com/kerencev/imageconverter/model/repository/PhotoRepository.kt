package com.kerencev.imageconverter.model.repository

import android.content.ContentResolver
import android.graphics.Bitmap
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface PhotoRepository {
    fun getImagesFromGallery(contentResolver: ContentResolver): Single<List<String>>
    fun convertImage(contentResolver: ContentResolver, path: String): Single<Bitmap>
    fun saveImage(contentResolver: ContentResolver, image: Bitmap): Completable
}