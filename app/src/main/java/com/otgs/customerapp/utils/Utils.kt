@file:Suppress("NAME_SHADOWING")

package com.otgs.customerapp.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.CharacterSetECI
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.otgs.customerapp.BuildConfig
import com.otgs.customerapp.Constants.Companion.IMAGE_URL
import com.otgs.customerapp.R
import com.otgs.customerapp.utils.FileUtils.MIME_TYPE_IMAGE
import com.otgs.customerapp.utils.FileUtils.getFile
import com.otgs.customerapp.utils.FileUtils.isDownloadsDocument
import com.otgs.customerapp.utils.FileUtils.isExternalStorageDocument
import com.otgs.customerapp.utils.FileUtils.isMediaDocument
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    //Our variables
    var mUri: Uri? = null

    //Our constants
    val OPERATION_CAPTURE_PHOTO = 1
    val OPERATION_CHOOSE_PHOTO = 2
    val OPERATION_CHOOSE_MULTIPLE_PHOTO = 3
    var source: File? = null

    val isRTL: Boolean
        get() = isRTL(Locale.getDefault())

    fun inputStreamToString(inputStream: InputStream): String? {
        try {
            val bytes = ByteArray(inputStream.available())

            inputStream.read(bytes, 0, bytes.size)

            return String(bytes)
        } catch (e: IOException) {
            return null
        }

    }

    fun toggleUpDownWithAnimation(view: View): Boolean {
        if (view.rotation == 0f) {
            view.animate().setDuration(150).rotation(180f)
            return true
        } else {
            view.animate().setDuration(150).rotation(0f)
            return false
        }
    }

    fun toggleUpDownWithAnimation(view: View, duration: Int, degree: Int) {

        view.animate().setDuration(duration.toLong()).rotation(degree.toFloat())

    }

    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     */
    fun milliSecondsToTimer(milliseconds: Long): String {
        var finalTimerString = ""
        val secondsString: String

        // Convert total duration into time
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        // Add hours if there
        if (hours > 0) {
            finalTimerString = "$hours:"
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0$seconds"
        } else {
            secondsString = "" + seconds
        }

        finalTimerString = "$finalTimerString$minutes:$secondsString"

        // return timer string
        return finalTimerString
    }

    /**
     * Function to get Progress percentage
     */
    fun getProgressPercentage(currentDuration: Long, totalDuration: Long): Int {
        val percentage: Double?

        val currentSeconds = (currentDuration / 1000).toInt().toLong()
        val totalSeconds = (totalDuration / 1000).toInt().toLong()

        // calculating percentage
        percentage = currentSeconds.toDouble() / totalSeconds * 100

        // return percentage
        return percentage.toInt()
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    fun progressToTimer(progress: Int, totalDuration: Int): Int {
        var totalDuration = totalDuration
        val currentDuration: Int
        totalDuration = totalDuration / 1000
        currentDuration = (progress.toDouble() / 100 * totalDuration).toInt()

        // return current duration in milliseconds
        return currentDuration * 1000
    }

    fun getDrawableInt(context: Context?, name: String?): Int {
        return context?.resources!!.getIdentifier(name, "drawable", context.packageName)
    }

    fun getResourceInt(context: Context?, defType: String, name: String): Int {
        return context?.resources!!.getIdentifier(name, defType, context.packageName)
    }

    fun setImageToImageView(context: Context, imageView: ImageView, drawable: Int) {
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
            .skipMemoryCache(true)

        Glide.with(context)
            .load(drawable)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    fun setCircleImageToImageView(
        context: Context?,
        imageView: ImageView,
        drawable: Int,
        borderWidth: Int,
        color: Int
    ) {
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
            .skipMemoryCache(true)
            .circleCrop()

        if (borderWidth > 0) {
            Glide.with(context!!)
                .load(drawable)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {

                        imageView.setImageDrawable(resource)

                        try {
                            val colorContextCompat = ContextCompat.getColor(context, color)


                            val bitmap = (resource as BitmapDrawable).bitmap

                            if (bitmap != null) {

                                val d = BitmapDrawable(
                                    context.resources,
                                    getCircularBitmapWithBorder(
                                        bitmap,
                                        borderWidth,
                                        colorContextCompat
                                    )
                                )

                                imageView.setImageDrawable(d)
                            } else {
                                imageView.setImageDrawable(resource)
                            }
                        } catch (e: Exception) {
                            Log.e("TEAMPS", "onResourceReady: ", e)
                        }

                    }
                })
        } else {
            Glide.with(context!!)
                .load(drawable)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }
    }

    fun setCircleImageToImageView(
        context: Context,
        imageView: ImageView,
        drawable: Int,
        drawableTintColor: Int,
        borderWidth: Int,
        color: Int
    ) {
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
            .skipMemoryCache(true)
            .circleCrop()

        if (borderWidth > 0) {
            Glide.with(context)
                .load(drawable)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {

                        imageView.setImageDrawable(resource)

                        try {
                            val colorContextCompat = ContextCompat.getColor(context, color)

                            var bitmap: Bitmap? = (resource as BitmapDrawable).bitmap

                            if (bitmap != null) {

                                val paint = Paint()
                                paint.colorFilter = PorterDuffColorFilter(
                                    ContextCompat.getColor(
                                        context,
                                        drawableTintColor
                                    ), PorterDuff.Mode.SRC_IN
                                )
                                val bitmapResult = Bitmap.createBitmap(
                                    bitmap.width,
                                    bitmap.height,
                                    Bitmap.Config.ARGB_8888
                                )

                                val canvas = Canvas(bitmapResult)
                                canvas.drawBitmap(bitmap, 0f, 0f, paint)

                                bitmap = bitmapResult

                                val d = BitmapDrawable(
                                    context.resources,
                                    getCircularBitmapWithBorder(
                                        bitmap,
                                        borderWidth,
                                        colorContextCompat
                                    )
                                )

                                imageView.setImageDrawable(d)
                            } else {
                                imageView.setImageDrawable(resource)
                            }
                        } catch (e: Exception) {
                            Log.e("TEAMPS", "onResourceReady: ", e)
                        }

                    }
                })
        } else {
            Glide.with(context)
                .load(drawable)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }
    }

    fun setCornerRadiusImageToImageView(
        context: Context,
        imageView: ImageView,
        drawable: Int,
        radius: Int,
        borderWidth: Int,
        color: Int
    ) {
        var requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
            .override(500, 500)
            .centerCrop()
            .skipMemoryCache(true)
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(radius))

        if (borderWidth > 0) {
            Glide.with(context)
                .load(drawable)

                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {

                        imageView.setImageDrawable(resource)

                        try {
                            val colorContextCompat = ContextCompat.getColor(context, color)

                            val bitmap = (imageView.drawable as BitmapDrawable).bitmap

                            if (bitmap != null) {
                                val d = BitmapDrawable(
                                    context.resources,
                                    getRoundedCornerBitmap(
                                        context,
                                        bitmap,
                                        colorContextCompat,
                                        radius,
                                        borderWidth
                                    )
                                )
                                imageView.setImageDrawable(d)
                            } else {
                                imageView.setImageDrawable(resource)
                            }
                        } catch (e: Exception) {
                            Log.e("TEAMPS", "onResourceReady: ", e)
                        }

                    }
                })
        } else {
            Glide.with(context)
                .load(drawable)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }
    }

    fun getCircularBitmapWithBorder(
        bitmap: Bitmap?,
        borderWidth: Int, color: Int
    ): Bitmap? {
        if (bitmap == null || bitmap.isRecycled) {
            return null
        }

        val width = bitmap.width + borderWidth
        val height = bitmap.height + borderWidth

        val canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = shader

        val canvas = Canvas(canvasBitmap)
        val radius = if (width > height) height.toFloat() / 2f else width.toFloat() / 2f
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)
        paint.shader = null
        paint.style = Paint.Style.STROKE
        paint.color = color
        paint.strokeWidth = borderWidth.toFloat()
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            radius - borderWidth / 2,
            paint
        )
        return canvasBitmap
    }

    private fun getRoundedCornerBitmap(
        context: Context,
        bitmap: Bitmap,
        color: Int,
        cornerDips: Int,
        borderDips: Int
    ): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width, bitmap.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)

        val borderSizePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, borderDips.toFloat(),
            context.resources.displayMetrics
        ).toInt()
        val cornerSizePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, cornerDips.toFloat(),
            context.resources.displayMetrics
        ).toInt()
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)

        // prepare canvas for transfer
        paint.isAntiAlias = true
        paint.color = -0x1
        paint.style = Paint.Style.FILL
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawRoundRect(rectF, cornerSizePx.toFloat(), cornerSizePx.toFloat(), paint)

        // draw bitmap
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        // draw border
        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderSizePx.toFloat()
        canvas.drawRoundRect(rectF, cornerSizePx.toFloat(), cornerSizePx.toFloat(), paint)

        return output
    }


    @SuppressLint("RestrictedApi")
    fun removeShiftMode(view: BottomNavigationView) {
        val menuView = view.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false
            for (i in 0 until menuView.childCount) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView
                item.setShifting(false)
                // set once again checked value, so view will be updated
                item.setChecked(item.itemData.isChecked)
            }
        } catch (e: NoSuchFieldException) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field")
        } catch (e: IllegalAccessException) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode")
        }

    }

    private fun isRTL(locale: Locale): Boolean {
        val directionality = Character.getDirectionality(locale.displayName[0]).toInt()
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT.toInt() || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC.toInt()
    }

    fun pxToDp(context: Context, px: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun updateMenuIconColor(menu: Menu, @ColorInt color: Int) {
        for (index in 0 until menu.size()) {
            val drawable = menu.getItem(index).icon
            drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    fun hideFirstFab(v: View) {
        v.visibility = View.GONE
        v.translationY = v.height.toFloat()
        v.alpha = 0f
    }

    fun twistFab(v: View, rotate: Boolean): Boolean {
        v.animate().setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                }
            })
            .rotation(if (rotate) 165f else 0f)
        return rotate
    }

    fun showFab(v: View) {
        v.visibility = View.VISIBLE
        v.alpha = 0f
        v.translationY = v.height.toFloat()
        v.animate()
            .setDuration(300)
            .translationY(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                }
            })
            .alpha(1f)
            .start()
    }

    fun hideFab(v: View) {
        v.visibility = View.VISIBLE
        v.alpha = 1f
        v.translationY = 0f
        v.animate()
            .setDuration(300)
            .translationY(v.height.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    v.visibility = View.GONE
                    super.onAnimationEnd(animation)
                }
            }).alpha(0f)
            .start()
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    fun getDeviceID(context: Context): String {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    var calendar = Calendar.getInstance();
    fun DateDialog(context: Activity, editText: EditText) {
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            var calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "MM-dd-yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            editText.setText(sdf.format(calendar.getTime()))
            editText.setEnabled(true)
            //updateLabel();
            val userAge: Calendar =
                GregorianCalendar(year, monthOfYear, dayOfMonth)
            val minAdultAge: Calendar = GregorianCalendar()
            minAdultAge.add(Calendar.YEAR, -18)
            if (minAdultAge.before(userAge)) {
                Toast.makeText(context, "Select valid Date", Toast.LENGTH_LONG).show()
            }
            //updateLabel();
        }
        val calendar1 = Calendar.getInstance()
        calendar1.add(Calendar.YEAR, -18)
        val dpDialog = DatePickerDialog(
            context, date,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dpDialog.datePicker.maxDate = calendar1.timeInMillis
        dpDialog.show()
    }

    @Throws(WriterException::class)
    fun generateQR(value: String): Bitmap? {
        val bitMatrix: BitMatrix
        try {
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            hints[EncodeHintType.CHARACTER_SET] = CharacterSetECI.UTF8

            bitMatrix = MultiFormatWriter().encode(
                value,
                BarcodeFormat.QR_CODE,
                500,
                500,
                hints
            )
        } catch (Illegalargumentexception: IllegalArgumentException) {
            return null
        }

        val bitMatrixWidth = bitMatrix.width

        val bitMatrixHeight = bitMatrix.height

        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth

            for (x in 0 until bitMatrixWidth) {

                pixels[offset + x] = if (bitMatrix.get(x, y))
                    Color.BLACK
                else
                    Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)

        return bitmap
    }

    fun capturePhoto(context: Activity) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(context.packageManager) != null) {
            source = getPictureFile(context)
            if (source != null) {
                val photoURI = FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    source!!
                )
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                context.startActivityForResult(cameraIntent, OPERATION_CAPTURE_PHOTO)
            }
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            context.startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)
        }
    }

    @Throws(IOException::class)
    private fun getPictureFile(context: Activity): File? {
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val pictureFile: String =
            context.getString(R.string.app_name).replace(" ", "").toString() + timeStamp
        val storageDir = File(
            Environment.getExternalStorageDirectory().absolutePath + File.separator + context.getString(
                R.string.app_name
            )
        )
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
//        val storageDir1: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image: File? = null
        try {
            image = File.createTempFile(pictureFile, ".jpg", storageDir)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }

    fun openGallery1(context: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        context.startActivityForResult(intent, OPERATION_CHOOSE_MULTIPLE_PHOTO)
    }

    fun openGallery(context: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        context.startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
    }

    @SuppressLint("NewApi")
    @Throws(URISyntaxException::class)
    fun getPath(context: Activity, uri: Uri): String? {
        var uri = uri
        val needToCheckUri = Build.VERSION.SDK_INT >= 19
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(
                context.getApplicationContext(),
                uri
            )
        ) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                return Environment.getExternalStorageDirectory()
                    .toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("image" == type) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
            }
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection =
                arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver
                    .query(uri, projection, selection, selectionArgs, null)
                val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor!!.moveToFirst()) {
                    return cursor!!.getString(column_index)
                }
            } catch (e: Exception) {
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    @NonNull
    fun prepareFilePart(
        context: Activity,
        partName: String,
        fileUri: Uri
    ): MultipartBody.Part? {
        val file: File? = getFile(context, fileUri)
        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse(MIME_TYPE_IMAGE), file)
        return MultipartBody.Part.createFormData(partName, file!!.name, requestFile)
    }

    fun loadImage(
        activity: Activity,
        type: Boolean,
        int: Int,
        image: String,
        imageView: ImageView
    ) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.format(DecodeFormat.PREFER_ARGB_8888)
        requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
        requestOptions = requestOptions.skipMemoryCache(true)
        requestOptions = requestOptions.signature(ObjectKey(System.currentTimeMillis().toString()))
        if (int == 1)
            requestOptions = RequestOptions.circleCropTransform()

        if (type)
            Glide.with(activity).load(IMAGE_URL + image).apply(requestOptions)
                .placeholder(R.drawable.baseline_image_black_24).into(imageView)
        else
            Glide.with(activity).load(image).apply(requestOptions)
                .placeholder(R.drawable.baseline_image_black_24).into(imageView)
    }

    fun loadImage(activity: Activity, int: Int, image: Uri, imageView: ImageView) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.format(DecodeFormat.PREFER_ARGB_8888)
        requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
        requestOptions = requestOptions.skipMemoryCache(true)
        requestOptions = requestOptions.signature(ObjectKey(System.currentTimeMillis().toString()))
        if (int == 1)
            requestOptions = RequestOptions.circleCropTransform()

        Glide.with(activity).load(image).apply(requestOptions)
            .placeholder(R.drawable.baseline_image_black_24).into(imageView)
    }

    fun dateDialog(context: Context, textView: TextView) {
        val cldr = Calendar.getInstance()
        val day = cldr[Calendar.DAY_OF_MONTH]
        val month = cldr[Calendar.MONTH]
        val year = cldr[Calendar.YEAR]
        // date picker dialog
        val picker = DatePickerDialog(
            context,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth -> textView.text = "" + (monthOfYear + 1) + "-" + dayOfMonth.toString() + "-" + year },
            year,
            month,
            day
        )
        picker.show();
    }
}
