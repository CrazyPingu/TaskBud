package com.mobile.todo

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mobile.todo.utils.Constant
import com.mobile.todo.utils.Monet
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.OutputStream
import kotlin.reflect.KClass


class Camera : AppCompatActivity() {

    companion object {
        lateinit var PAGE_TO_RETURN: KClass<*>
    }

    private lateinit var profilePic: ImageView
    private lateinit var selectCamera: ImageView
    private lateinit var selectGallery: ImageView
    private lateinit var confirmButton: Button
    private lateinit var cancelButton: Button

    private lateinit var profilePicImage: Uri

    private var profilePicImagePassed: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_pic)

        profilePic = findViewById(R.id.profile_pic)
        selectCamera = findViewById(R.id.select_camera)
        selectGallery = findViewById(R.id.select_gallery)
        confirmButton = findViewById(R.id.confirm)
        cancelButton = findViewById(R.id.cancel)

        if (intent.hasExtra("profilePic")) {
            profilePicImagePassed = intent.getParcelableExtra<Uri>("profilePic")
            profilePic.setImageURI(intent.getParcelableExtra<Uri>("profilePic"))
        }

        selectCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            setPhoto.launch(intent)
        }

        selectGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            setPhoto.launch(intent)
        }

        cancelButton.setOnClickListener {
            intent = Intent(this, Camera.PAGE_TO_RETURN.java)
            if (profilePicImagePassed != null) {
                intent.putExtra("profilePic", profilePicImagePassed)
            }
            startActivity(intent)
        }

        confirmButton.setOnClickListener {
            intent = Intent(this, Camera.PAGE_TO_RETURN.java)
            if (::profilePicImage.isInitialized) {
                intent.putExtra("profilePic", profilePicImage)
            }
            startActivity(intent)
        }

        if(Constant.getMonet(this)){
            Monet.setButtonMonet(confirmButton, this)
            Monet.setButtonMonet(cancelButton, this)
            Monet.setStatusBarMonet(this, window)
        }
    }

    private var setPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                profilePicImage = if (data?.extras?.getParcelable<Bitmap>("data") is Bitmap) {
                    getImageUri(data.extras?.get("data") as Bitmap)
                } else {
                    data?.data!!.let { uri ->
                        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                        getImageUri(bitmap)
                    }
                }
                profilePic.setImageURI(profilePicImage)
            }
        }




    private fun getImageUri(inImage: Bitmap): Uri {
        val fileName = "ProfilePic_" + System.currentTimeMillis() + ".jpeg"

        this.openFileOutput(fileName, MODE_PRIVATE).use { outStream ->
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        }

        return Uri.fromFile(File(filesDir, fileName))
    }






    override fun onBackPressed() {
        intent = Intent(this, Camera.PAGE_TO_RETURN.java)
        if (profilePicImagePassed != null) {
            intent.putExtra("profilePic", profilePicImagePassed)
        }
        startActivity(intent)
    }
}