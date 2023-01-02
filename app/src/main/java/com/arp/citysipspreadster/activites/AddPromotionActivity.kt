package com.arp.citysipspreadster.activites

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivityAddPromotionBinding
import com.arp.citysipspreadster.model.promotion.ResponseAddPromotion
import com.arp.citysipspreadster.utils.*
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mahdiasd.filepicker.FileModel
import com.mahdiasd.filepicker.FilePicker
import com.mahdiasd.filepicker.FilePickerListener
import com.mahdiasd.filepicker.PickerMode
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

class AddPromotionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPromotionBinding
    var pd: ProgressDialog? = null
    private var address: String? = null
    private var percentage: String? = null
    private var offerTitle: String? = null
    private var businessName: String? = null
    private var offerId: String? = null
    private var sessionManager: SessionManager? = null

    var name = ""
    var img2 = ""
   /* val REQUEST_IMAGE = 100
    var flag_image = 0
    lateinit var bitmap2: Bitmap*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            binding = ActivityAddPromotionBinding.inflate(layoutInflater)
            setContentView(binding.root)

            sessionManager = SessionManager(this)

        binding.toolbar.toolbarBack.setOnClickListener {
            val intent = Intent(this@AddPromotionActivity, NewPromotionDetailsActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        businessName = intent.getStringExtra("businessName")
        percentage = intent.getStringExtra("percentage")
        offerTitle = intent.getStringExtra("offerTitle")
        address = intent.getStringExtra("address")
        binding.txtBusinessName.text = businessName
        binding.txtPercentage.text = percentage
        binding.txtOfferTitle.text = offerTitle
        binding.txtAddress.text = address
       // offerId = intent.getStringExtra("offerId")

        /*if (businessName == "") {

            binding.txtBusinessName.text = ""

        } else {

            binding.txtBusinessName.text = businessName
        }

        if (percentage == "") {

            binding.txtPercentage.text = ""

        } else {

            binding.txtPercentage.text = percentage
        }

        if (offerTitle == "") {

            binding.txtOfferTitle.text = ""

        } else {

            binding.txtOfferTitle.text = offerTitle
        }

        if (address == "") {

            binding.txtAddress.text = ""

        } else {

            binding.txtAddress.text = address
        }
*/
        binding.txtFileName.setOnClickListener {

           // onProfileImageClick()
            FilePicker(this, supportFragmentManager)
                .setMode(PickerMode.Video, PickerMode.Image)
                //PickerMode.Audio,PickerMode.FILE
                .setDefaultMode(PickerMode.Image)
                .setMaxSelection(1)
                .setMaxEachFileSize(1 * 1000) // mean -> 1 mb
                .setMaxTotalFileSize(15 * 1000) // mean -> 15 mb
                .setCustomText("Video", "","","Image")
                //, "storage", "image", "openStorage"
                .setShowFileWhenClick(true)
                .setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                .setDeActiveColor(ContextCompat.getColor(this, R.color.clr_BFBFBF))
                .setActiveColor(ContextCompat.getColor(this, R.color.clr_EA2A31))
                .setIcons(
                    videoIcon = ContextCompat.getDrawable(this, com.mahdiasd.filepicker.R.drawable.ic_video),
                    audioIcon = ContextCompat.getDrawable(this, com.mahdiasd.filepicker.R.drawable.ic_audio),
                    // imageIcon = ContextCompat.getDrawable(this, com.mahdiasd.filepicker.R.drawable.ic_image),
                    //  fileManagerIcon = ContextCompat.getDrawable(this, com.mahdiasd.filepicker.R.drawable.ic_file),
                )
                .setListener(object : FilePickerListener {
                    override fun selectedFiles(list: List<FileModel>?) {

                        if (list!!.isNotEmpty()) {

                            Log.e("File Path :", list[0].file.path)
                            img2 = list[0].file.path.toString()
                            name = list[0].file.name

                            Log.e("TAG....", "Image cache path: ${list[0].file}")


                            if (img2 != "") {

                                binding.txtFileName.text = list[0].file.name
                                binding.txtFileName.setTextColor(resources.getColor(R.color.clr_EA2A31));
                                binding.llClose.visibility = View.VISIBLE

                            }
                        }
                    }
                })
                .show()


        }

        binding.imgClose.setOnClickListener {

            binding.txtFileName.text = ".jpg, .png, .mp4, .mkv only"
            binding.txtFileName.setTextColor(resources.getColor(R.color.clr_999999));
            binding.llClose.visibility = View.GONE
            img2 = ""
        }
        
        binding.btnPreview.setOnClickListener {
            Toast.makeText(this, "Preview Clicked...", Toast.LENGTH_SHORT).show()
        }

        binding.edtPromoCode.filters += InputFilter.LengthFilter(7)


        binding.btnSpreadNow.setOnClickListener {

            if (binding.edtPromotionTitle.text.toString() == "") {

                binding.edtPromotionTitle.error = "Please enter promotion title"

            } else if (binding.edtDescription.text.toString() == "") {

                binding.edtDescription.error = "Please enter promotion title"

            } else if (binding.edtPromoCode.text.toString() == "") {

                binding.edtPromoCode.error = "Please enter promo code"

            } else {


              /*  val intent = Intent(this@AddPromotionActivity, SharePromotionActivity::class.java)
                startActivity(intent)*/
                addPromotion()
            }

        }


    }

    private fun addPromotion() {

        pd = ProgressDialog(this@AddPromotionActivity)
        pd!!.setMessage("Loading...")
        pd!!.setCancelable(false)
        pd!!.show()

        val authHeader = "Bearer " + WS_URL_PARAMS.createJWT(
            WS_URL_PARAMS.issuer,
            WS_URL_PARAMS.subject
        ) as String
        val accesskey: String = WS_URL_PARAMS.access_key
        val sid: String = sessionManager!!.getUserId().toString()
        val offerId: String = sessionManager!!.getPromotionId().toString()
        val title: String = binding.edtPromotionTitle.text.toString()
        val description: String = binding.edtDescription.text.toString()
        val promoCode: String =binding.edtPromoCode.text.toString()

        val t1 = RequestBody.create(MediaType.parse("multipart/form-data"), authHeader)
        val t2 = RequestBody.create(MediaType.parse("multipart/form-data"), accesskey)
        val t3 = RequestBody.create(MediaType.parse("multipart/form-data"), sid)
        val t4 = RequestBody.create(MediaType.parse("multipart/form-data"), offerId)
        val t5 = RequestBody.create(MediaType.parse("multipart/form-data"), title)
        val t6 = RequestBody.create(MediaType.parse("multipart/form-data"), description)
        val t7 = RequestBody.create(MediaType.parse("multipart/form-data"), promoCode)

        var file: File? = null
        var body1: MultipartBody.Part? = null

        if (img2 != "") {

            file = File(img2)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val s = name + "_" + file.name
            val logo = s.replace(" ".toRegex(), "_")

            body1 = MultipartBody.Part.createFormData("image", logo, requestFile)

        }

       /* var file: File? = null
        var body1: MultipartBody.Part? = null
        if (img2 != "") {
            file = File(img2)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val s = title + "_" + file.name
            val logo = s.replace(" ".toRegex(), "_")
            body1 = MultipartBody.Part.createFormData("image", logo, requestFile)
        }

        assert(body1 != null)
        Log.e("Image File: ", file!!.name)*/

        Log.e("Multipart Body File :", img2.toString())
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseAddPromotion?>? = api.addPromotion(
            "Bearer " + WS_URL_PARAMS.createJWT(
                WS_URL_PARAMS.issuer,
                WS_URL_PARAMS.subject
            ), t2, t3, t4, t5, t6, t7, body1
        )

        call!!.enqueue(object : Callback<ResponseAddPromotion?> {
            override fun onResponse(
                call: Call<ResponseAddPromotion?>,
                response: Response<ResponseAddPromotion?>
            ) {

                Log.e("responseAddPromotion", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(
                            this@AddPromotionActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        val intent = Intent(this@AddPromotionActivity, SharePromotionActivity::class.java)
                        intent.putExtra("flagNav","1")
                        startActivity(intent)

                    }

                } else {

                    Toast.makeText(
                        this@AddPromotionActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseAddPromotion?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }


   /* fun onProfileImageClick() {
        Dexter.withActivity(this@AddPromotionActivity)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showImagePickerOptions()
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, object :
            ImagePickerActivity.PickerOptionListener {
            override fun onTakeCameraSelected() {
                launchCameraIntent()
            }

            override fun onChooseGallerySelected() {
                launchGalleryIntent()
            }
        })
    }

    private fun launchCameraIntent() {
        val intent = Intent(this@AddPromotionActivity, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        startActivityForResult(
            intent,
            REQUEST_IMAGE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                var selectedImage = data.data
                if (uri != null) {
                    try {
                        // You can update this bitmap to your server
                        bitmap2 = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                        selectedImage = getImageUri(applicationContext, bitmap2)
                        Log.e("uripath", "" + selectedImage)
                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor = contentResolver.query(
                            selectedImage!!,
                            filePathColumn, null, null, null
                        )
                        cursor!!.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        img2 = cursor.getString(columnIndex)

                        var file = File(img2)
                        binding.txtFileName.text = file.name
                        binding.txtFileName.setTextColor(resources.getColor(R.color.clr_EA2A31));
                        binding.llClose.visibility = View.VISIBLE

                        cursor.close()
                        Log.e("path", img2)
                        // loading profile image from local cache
                        loadProfile(uri.toString())
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title" + Calendar.getInstance().time,
            null
        )
        return Uri.parse(path)
    }

    private fun loadProfile(url: String) {
        Log.e("TAG....", "Image cache path: $url")


        //img2 = url;

        *//*Glide.with(this).load(url)
                .into(binding.imglogoPic);
        binding.imglogoPic.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));*//*
       *//* Glide.with(
            this
        ).load(url)
            .into(binding.imgDishLogo)
        binding.imgDishLogo.setColorFilter(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )*//*
    }

    private fun launchGalleryIntent() {
        val intent = Intent(this@AddPromotionActivity, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(
            intent,
           REQUEST_IMAGE
        )
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@AddPromotionActivity)
        builder.setTitle(getString(R.string.dialog_permission_title))
        builder.setMessage(getString(R.string.dialog_permission_message))
        builder.setPositiveButton(getString(R.string.go_to_settings)) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            getString(android.R.string.cancel)
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }*/
}