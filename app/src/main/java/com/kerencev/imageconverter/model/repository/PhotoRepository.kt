package com.kerencev.imageconverter.model.repository

import android.content.Context

interface PhotoRepository {
    fun getImagesFromGallery(context: Context): List<String>
}