package com.mobile.todo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import kotlin.reflect.KClass
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mobile.todo.utils.Permission
import java.io.ByteArrayOutputStream


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
    }

    private var setPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                profilePicImage = if (data?.extras?.getParcelable<Bitmap>("data") is Bitmap) {
                    getImageUri(data.extras?.get("data") as Bitmap)
                } else {
                    data?.data!!
                }
                profilePic.setImageURI(profilePicImage)
            }
        }

    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            this.contentResolver,
            inImage,
            null,
            null
        )
        return Uri.parse(path)
    }


    override fun onBackPressed() {
        intent = Intent(this, Camera.PAGE_TO_RETURN.java)
        if (profilePicImagePassed != null) {
            intent.putExtra("profilePic", profilePicImagePassed)
        }
        startActivity(intent)
    }
}