package com.kerencev.imageconverter.presenter

import android.content.Context
import android.util.Log
import com.kerencev.imageconverter.model.repository.PhotoRepository
import com.kerencev.imageconverter.utils.Converter
import com.kerencev.imageconverter.utils.disposeBy
import com.kerencev.imageconverter.utils.subscribeByDefault
import com.kerencev.imageconverter.view.MainView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter

class MainPresenter(
    private val context: Context,
    private val repository: PhotoRepository
) : MvpPresenter<MainView>() {

    private val bag = CompositeDisposable()
    private var disposable: Disposable? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.checkPermission()
    }

    fun loadImages() {
        repository.getImagesFromGallery(context)
            .subscribeByDefault()
            .subscribe(
                {
                    viewState.initList(it)
                },
                {
                    Log.d("getImagesFromGallery", "${it.stackTrace}")
                }
            ).disposeBy(bag)
    }

    fun convertImage(path: String) {
        viewState.showSnackBar(path)
        disposable = Converter.convertImage(context, path)
            .flatMap {
                Converter.saveImage(context, it).toSingle {}
            }
            .subscribeByDefault()
            .subscribe(
                {
                    loadImages()
                    viewState.hideSnackBar()
                },
                {
                    Log.d("convertImage", "${it.stackTrace}")
                    viewState.hideSnackBar()
                }
            )
    }

    fun disposeConvert() {
        disposable?.dispose()
    }

    override fun onDestroy() {
        disposable?.dispose()
        bag.dispose()
        super.onDestroy()
    }
}