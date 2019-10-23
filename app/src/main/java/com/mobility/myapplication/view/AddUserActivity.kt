package com.mobility.myapplication.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.mobility.myapplication.BuildConfig
import com.mobility.myapplication.R
import com.mobility.myapplication.view.MainActivity.Companion.AVATAR_URL_USER
import com.mobility.myapplication.view.MainActivity.Companion.INSERT_REQUEST_CODE
import com.mobility.myapplication.view.MainActivity.Companion.LOGIN_USER
import com.mobility.myapplication.view.MainActivity.Companion.TYPE_USER
import kotlinx.android.synthetic.main.activity_add_user.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddUserActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 1
    private var currentPhotoPath: String? = null
    private var fileUri: Uri? = null
    private val CAMERA_REQUEST = 1888
    private var userTypeList: Array<String>? = null
    private var selectedUserType: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        bindSpinnerData()
        addImageButton.setOnClickListener {
            openCamera()
        }
        userTypeSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                selectedUserType = userTypeList!![position]
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })

    }


    private fun bindSpinnerData() {
        userTypeList = resources.getStringArray(R.array.userType)

        val adapterUser =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypeList!!)
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userTypeSpinner.adapter = adapterUser
    }

    private fun openCamera() {
        if (!checkPermission()) {
            requestPermission()
        } else {
            startCamera()
        }
    }


    private fun getOutputMediaFileUri(): Uri {
        return FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + ".provider",
            getOutputMediaFile()
        )

    }

    private fun getOutputMediaFile(): File {
        val sdDir = Environment.getExternalStorageDirectory()
        val mediaStorageDir = File(sdDir.absolutePath + File.separator + "User")
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdir()
        }
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val mediaFile: File
        mediaFile =
            File(mediaStorageDir.absolutePath + File.separator + "IMG_" + timeStamp + ".jpg")
        currentPhotoPath = "file:" + mediaFile.absolutePath
        return mediaFile
    }

    private fun checkPermission(): Boolean {
        val storageResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val cameraResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        return storageResult == PackageManager.PERMISSION_GRANTED && cameraResult == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        when {
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> Toast.makeText(
                this,
                resources.getText(R.string.camera_permission_reason),
                Toast.LENGTH_LONG
            ).show()
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> Toast.makeText(
                this,
                resources.getText(R.string.storage_permission_reason),
                Toast.LENGTH_LONG
            ).show()
            else -> ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri = Uri.parse(currentPhotoPath)
            Toast.makeText(this, imageUri.toString(), Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(LOGIN_USER, userNameEditText.text.toString())
            intent.putExtra(TYPE_USER, selectedUserType)
            intent.putExtra(AVATAR_URL_USER, imageUri)
            setResult(INSERT_REQUEST_CODE!!, intent)
            finish()
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fileUri = getOutputMediaFileUri()
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(intent, CAMERA_REQUEST)
    }


}
