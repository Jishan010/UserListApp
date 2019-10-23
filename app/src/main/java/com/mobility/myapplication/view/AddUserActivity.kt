package com.mobility.myapplication.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.util.Log
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
import com.mobility.myapplication.view.MainActivity.Companion.LOGIN_USER
import com.mobility.myapplication.view.MainActivity.Companion.TYPE_USER
import kotlinx.android.synthetic.main.activity_add_user.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddUserActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 1
    private var currentPhotoPath: String? = null
    private var userName: String? = null
    private val CAMERA_REQUEST = 1888
    private var userTypeList: Array<String>? = null
    private var selectedUserType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        bindSpinnerData()
        userName = userNameEditText.text.toString()
        addImageButton.setOnClickListener {
            openCamera()
        }

        userTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        }

        addUserButton.setOnClickListener {

            if (selectedUserType != null && currentPhotoPath != null && userName != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(LOGIN_USER, userName)
                intent.putExtra(TYPE_USER, selectedUserType)
                intent.putExtra(AVATAR_URL_USER, currentPhotoPath)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    resources.getText(R.string.warning_msg),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

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
            launchCamera()
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
        currentPhotoPath = image.absolutePath
        return image
    }


    private fun launchCamera() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(packageManager) != null) {
            //Create a file to store the image
            var photoFile: File? = null;
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {

            }
            if (photoFile != null) {
                val photoURI =
                    FileProvider.getUriForFile(
                        this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile
                    )
                pictureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    photoURI
                )
                startActivityForResult(
                    pictureIntent,
                    CAMERA_REQUEST
                )
            }
        }
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
                launchCamera()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d("ImagePath", currentPhotoPath)
        }
    }

}
