package com.kerencev.imageconverter.presenter

import android.content.Context
import android.util.Log
import com.kerencev.imageconverter.model.repository.PhotoRepository
import com.kerencev.imageconverter.utils.Converter
import com.kerencev.imageconverter.view.MainView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter

class MainPresenter(
    private val context: Context,
    private val repository: PhotoRepository
) : MvpPresenter<MainView>() {

    private var disposable: Disposable? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.checkPermission()
    }

    fun loadImages() {
        repository.getImagesFromGallery(context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    viewState.initList(it)
                },
                {
                    Log.d("getImagesFromGallery", "${it.message}")
                }
            )
    }

    fun convertImage(path: String) {
        disposable = Converter.convertImage(context, path)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Converter.saveImage(context, it)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        loadImages()
                        viewState.hideSnackBar()
                    }
                    .subscribe()
            }
            .doOnError {
                Log.d("ConverterSingle", "${it.message}")
                viewState.hideSnackBar()
            }
            .subscribe()
        viewState.showSnackBar(path)
    }

    fun disposeConvert() {
        disposable?.dispose()
    }
}