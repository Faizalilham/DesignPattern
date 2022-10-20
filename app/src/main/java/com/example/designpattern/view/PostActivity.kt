package com.example.designpattern.view

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.designpattern.MainActivity
import com.example.designpattern.databinding.ActivityPostBinding
import com.example.designpattern.viewmodel.CarsViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*

class PostActivity : AppCompatActivity() {
    private var _binding : ActivityPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var uri : Uri
    private var getFile: File? = null
    private lateinit var carsViewModel: CarsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPostBinding.inflate(layoutInflater)
        carsViewModel = ViewModelProvider(this)[CarsViewModel::class.java]
        setContentView(binding.root)
        postData()
        val id = intent.getIntExtra("id",0)
        checkingIntentId(id)
    }

    private fun checkingIntentId(id :Int){
        when(id){
            0 -> {
                binding.btnUpdate.visibility = View.GONE
                binding.btnPost.visibility = View.VISIBLE
                binding.btnPost.setOnClickListener {
                    upload()
                }
            }
            else ->{
                binding.btnPost.visibility = View.GONE
                binding.btnUpdate.visibility = View.VISIBLE
                binding.btnUpdate.setOnClickListener {
                    update(id)
                }
            }
        }
    }



    private fun update(id :Int){
        val name = binding.etNameCar.text.toString().trim()
        val category = binding.etNameCar.text.toString().trim()
        val price = binding.etNameCar.text.toString().trim()
        val status = binding.etNameCar.text.toString().trim()
        carsViewModel.updateDatasConfig(id,name,category,price.toInt(),status.toBoolean())
        carsViewModel.updateLiveData().observe(this){
            if(it != null){
                Toast.makeText(this, "Update Success!", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return file
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createFile(context)
        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private val openGallery = registerForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null){
            val myFile = uriToFile(it, this)
            getFile = myFile
            binding.imageCar.setImageURI(it)
        }
    }
    private fun openGallery(){
        intent.type = "image/*"
        openGallery.launch("image/*")
    }

    private val openCamera = registerForActivityResult(ActivityResultContracts.TakePicture()){
        if(it){
            binding.imageCar.setImageURI(uri)
        }
    }

    private fun createFile(context: Context): File {
        return File.createTempFile(
            "IMG_",
            ".jpg",
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
    }
    private fun openCamera(){
        uri = FileProvider.getUriForFile(this,"${this.packageName}.provider",createFile(this))
        getFile = uriToFile(uri,this)
        openCamera.launch(uri)
    }

    private fun postData(){
        binding.fab.setOnClickListener {
            openAlert()
        }
    }

    private fun openAlert(){
        val alert = AlertDialog.Builder(this)
        alert.apply {
            setMessage("Choose from ")
            setPositiveButton("Gallery"){_,_ ->
                openGallery()
            }
            setNegativeButton("Camera"){_,_ ->
                openCamera()
            }
            setNeutralButton("Cancel"){_,_ ->}
        }
        alert.show()
    }

    private fun upload(){
        if(getFile != null){
            val file = reduceFileImage(getFile as File)

            val name = binding.etNameCar.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val category = binding.etCategoryCar.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val price = binding.etPriceCar.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val status = binding.etStatusCar.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val currentImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                currentImageFile
            )
            carsViewModel.postData(name,category,price,status,imageMultipart)
            carsViewModel.postLiveData().observe(this){
                if(it != null){
                    Toast.makeText(this, "Upload Success!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java).also{
                        finish()
                    })
                }else{
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}