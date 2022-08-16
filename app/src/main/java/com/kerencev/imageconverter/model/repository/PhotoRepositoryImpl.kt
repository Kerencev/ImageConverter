package com.kerencev.imageconverter.model.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

class PhotoRepositoryImpl : PhotoRepository {
    override fun getImagesFromGallery(context: Context): List<String> {
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String

        val projection =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        val orderBy = MediaStore.Video.Media.DATE_TAKEN
        cursor = context.contentResolver.query(uri, projection, null, null, "$orderBy DESC")!!

        val columnIndexData: Int = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        val columnIndexFolderName: Int =
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(columnIndexData)
            listOfAllImages.add(absolutePathOfImage)
        }

        return listOfAllImages
    }
}