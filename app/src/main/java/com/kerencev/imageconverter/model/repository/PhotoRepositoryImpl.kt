package com.kerencev.imageconverter.model.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val HIGH_QUALITY = 100

class PhotoRepositoryImpl : PhotoRepository {
    @SuppressLint("Recycle")
    override fun getImagesFromGallery(contentResolver: ContentResolver): Single<List<String>> =
        Single.create {
            try {
                val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val cursor: Cursor
                val listOfAllImages = ArrayList<String>()
                var absolutePathOfImage: String

                val projection =
                    arrayOf(
                        MediaStore.MediaColumns.DATA,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME
                    )

                val orderBy = MediaStore.Video.Media.DATE_TAKEN
                cursor = contentResolver.query(uri, projection, null, null, "$orderBy DESC")!!

                val columnIndexData: Int =
                    cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

                while (cursor.moveToNext()) {
                    absolutePathOfImage = cursor.getString(columnIndexData)
                    listOfAllImages.add(absolutePathOfImage)
                }
                it.onSuccess(listOfAllImages)
            } catch (e: IOException) {
                it.onError(e)
            }
        }

    override fun convertImage(contentResolver: ContentResolver, path: String): Single<Bitmap> =
        Single.create {
            try {
                val tempConvertedFile = File.createTempFile("tmpConvert", ".png")
                val uri = Uri.fromFile(File(path))
                val fos = FileOutputStream(tempConvertedFile)
                val mim =
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                mim.compress(Bitmap.CompressFormat.PNG, HIGH_QUALITY, fos)
                fos.close()
                it.onSuccess(mim)
            } catch (e: IOException) {
                it.onError(e)
            }
        }

    override fun saveImage(contentResolver: ContentResolver, image: Bitmap): Completable =
        Completable.create {
            try {
                MediaStore.Images.Media.insertImage(
                    contentResolver,
                    image,
                    "${System.currentTimeMillis()}",
                    ""
                )
                it.onComplete()
            } catch (e: IOException) {
                it.onError(e)
            }
        }
}