package com.pascal.dansandroid.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.compose.ui.text.AnnotatedString
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.text.HtmlCompat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun AnnotatedString.Builder.appendHtmlText(htmlText: String) {
    val spanned = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
    append(spanned.toString())
}

fun openWebPage(context: Context, url: String) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    context.startActivity(intent)
}

fun JSONObject.toRequestBody(): RequestBody {
    return this.toString().toRequestBody("application/json".toMediaTypeOrNull())
}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}

fun Uri.toFile(context: Context): File? {
    return try {
        val contentResolver = context.contentResolver
        val fileName = "${System.currentTimeMillis()}." + getFileExtension(contentResolver)

        val file = File(context.cacheDir, fileName)

        contentResolver.openInputStream(this)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        file
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

@Throws(IOException::class)
private fun Uri.getFileExtension(contentResolver: ContentResolver): String {
    var extension: String? = null

    if (ContentResolver.SCHEME_CONTENT == scheme && contentResolver.getType(this)?.startsWith("image/") == true) {
        val cursor: Cursor? = contentResolver.query(this, arrayOf(MediaStore.Images.ImageColumns.MIME_TYPE), null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE)
                if (columnIndex != -1) {
                    val mimeType = it.getString(columnIndex)
                    extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                }
            }
        }
    }

    if (extension.isNullOrEmpty()) {
        extension = MimeTypeMap.getFileExtensionFromUrl(toString())
    }

    if (extension.isNullOrEmpty()) {
        extension = "jpg"
    }

    return extension!!
}