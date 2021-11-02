package com.aoc4456.radarchart.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.aoc4456.radarchart.R
import java.io.ByteArrayOutputStream

object ImageUtil {

    @Suppress("DEPRECATION")
    fun uriToBitmap(contentResolver: ContentResolver, uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream)
        return outputStream.toByteArray()
    }

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}

@BindingAdapter("iconImageBitmap")
fun setImageBitmap(imageView: ImageView, bitmap: Bitmap?) {
    if (bitmap == null) {
        val drawable = ResourcesCompat.getDrawable(
            imageView.context.resources,
            R.drawable.ic_baseline_remove_circle_outline_24,
            null
        )
        imageView.setImageDrawable(drawable)
    } else {
        imageView.setImageBitmap(bitmap)
    }
}
