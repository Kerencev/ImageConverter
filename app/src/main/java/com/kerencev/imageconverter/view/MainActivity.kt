package com.kerencev.imageconverter.view

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kerencev.imageconverter.R
import com.kerencev.imageconverter.databinding.ActivityMainBinding
import com.kerencev.imageconverter.model.repository.PhotoRepositoryImpl
import com.kerencev.imageconverter.presenter.MainPresenter
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

private const val MY_READ_PERMISSION_CODE = 101

class MainActivity : MvpAppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding
    private val presenter: MainPresenter by moxyPresenter {
        MainPresenter(
            contentResolver,
            PhotoRepositoryImpl()
        )
    }
    private var isClickableRecycler = true
    private val adapter = GalleryAdapter(object : OnItemClick {
        override fun onClick(path: String) {
            if (isClickableRecycler) {
                isClickableRecycler = false
                presenter.convertImage(path)
            }
        }
    })
    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvGallery.adapter = adapter
        snackbar = Snackbar.make(binding.root, "", Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.cancel) {
                presenter.disposeConvert()
                isClickableRecycler = true
            }
    }

    override fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE).toTypedArray(),
                MY_READ_PERMISSION_CODE
            )
        } else {
            presenter.loadImages()
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSnackBar(path: String) {
        snackbar.setText("${resources.getString(R.string.converting)}\n$path")
        snackbar.show()
    }

    override fun hideSnackBar() {
        snackbar.dismiss()
        isClickableRecycler = true
    }

    override fun setClickableRecycler(boolean: Boolean) {
        binding.rvGallery.isClickable = boolean
    }

    @SuppressLint("SetTextI18n")
    override fun initList(listOfPhotos: List<String>) = with(binding) {
        tvAllPhotos.text = "${resources.getString(R.string.all_photos)} ${listOfPhotos.size}"
        adapter.setData(listOfPhotos)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_READ_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "???????????????? ???????????? ?? ??????????????", Toast.LENGTH_SHORT).show()
                presenter.loadImages()
            } else {
                Toast.makeText(this, "?????? ?????????????? ?? ??????????????", Toast.LENGTH_SHORT).show()
            }
        }
    }
}