package com.kerencev.imageconverter.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
            this,
            PhotoRepositoryImpl()
        )
    }
    private val adapter = GalleryAdapter(object : OnItemClick {
        override fun onClick(path: String) {
            Toast.makeText(this@MainActivity, path, Toast.LENGTH_SHORT).show()
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

    override fun initList(listOfPhotos: List<String>) = with(binding) {
        tvAllPhotos.append(" ${listOfPhotos.size}")
        rvGallery.adapter = adapter
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
                Toast.makeText(this, "Разрешен доступ к галерее", Toast.LENGTH_SHORT).show()
                presenter.loadImages()
            } else {
                Toast.makeText(this, "Нет доступа к галерее", Toast.LENGTH_SHORT).show()
            }
        }
    }
}