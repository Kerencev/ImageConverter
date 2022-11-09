package com.kerencev.imageconverter.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.io.File
import java.io.FileOutputStream

object Converter {
    fun convertImage(context: Context, path: String): Single<Bitmap> {
        return Single.create {
            try {
                val tempConvertedFile = File.createTempFile("tmpConvert", ".png")
                val uri = Uri.fromFile(File(path))
                val fos = FileOutputStream(tempConvertedFile)
                val mim =
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                mim.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.close()
                it.onSuccess(mim)
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    fun saveImage(context: Context, image: Bitmap): Completable {
        return Completable.create {
            try {
                MediaStore.Images.Media.insertImage(
                    context.contentResolver,
                    image,
                    "${System.currentTimeMillis()}",
                    ""
                )
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }
}