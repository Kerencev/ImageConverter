package com.kerencev.imageconverter.presenter

import android.content.Context
import com.kerencev.imageconverter.model.repository.PhotoRepository
import com.kerencev.imageconverter.view.MainView
import moxy.MvpPresenter

class MainPresenter(
    private val context: Context,
    private val repository: PhotoRepository
) : MvpPresenter<MainView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.checkPermission()
    }

    fun loadImages() {
        val data = repository.getImagesFromGallery(context)
        viewState.initList(data)
    }
}