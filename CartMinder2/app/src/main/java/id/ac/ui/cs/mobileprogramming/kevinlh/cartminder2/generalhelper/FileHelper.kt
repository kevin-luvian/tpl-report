package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.generalhelper

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.BuildConfig
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


object FileHelper {
    private const val TAG: String = "FileHelper"

    fun createImageFile(context: Context, filename: String) =
        File("${context.filesDir}/$filename.jpg")

    fun createImageUri(context: Context, file: File) =
        FileProvider.getUriForFile(
            context, "${BuildConfig.APPLICATION_ID}.fileProvider", file
        )

    fun saveFile(file: File, bm: Bitmap) {
        val out: FileOutputStream
        //        File file = new File(cacheStorage, filename);
//        println("Filepath: " + file.absolutePath)
        try {
            out = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "exception: File is not created")
            e.printStackTrace()
            return
        }
        try {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
        } catch (e: NullPointerException) {
            Log.e(TAG, "exception: bitmap is null")
            e.printStackTrace()
        }
        try {
            out.flush()
            out.close()
        } catch (e: IOException) {
            Log.e(TAG, "exception: File is not successfully written")
            e.printStackTrace()
        }
    }

    fun deleteFiles(context: Context, filename: String) {
        val file = createImageFile(context, filename)
        if (file.exists()) file.delete()
    }

    fun getScaledBitmap(targetScale: Int, path: String): Bitmap {
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        val scaleFactor = 1.coerceAtLeast((photoW / targetScale).coerceAtMost(photoH / targetScale))
        Log.d(TAG, "target scale: $targetScale")
        Log.d(TAG, "photoWidth: $photoW photoHeight: $photoH")
        Log.d(TAG, "cale Factor: $scaleFactor")

        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        //        bmOptions.inPurgeable = true;
        return BitmapFactory.decodeFile(path, bmOptions)
    }

    /*  Code from: SANJAY GUPTA
        at https://stackoverflow.com/questions/17546101/get-real-path-for-uri-android */
    fun getPathFromUri(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return "${Environment.getExternalStorageDirectory()}/${split[1]}"
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri: Uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            uri?.let {
                cursor = context.contentResolver.query(
                    it, projection, selection, selectionArgs, null
                )
            }
            cursor?.let { cur ->
                if (cur.moveToFirst()) {
                    val index = cur.getColumnIndexOrThrow(column)
                    return cur.getString(index)
                }
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean =
        "com.android.externalstorage.documents" == uri.authority

    private fun isDownloadsDocument(uri: Uri): Boolean =
        "com.android.providers.downloads.documents" == uri.authority

    private fun isMediaDocument(uri: Uri): Boolean =
        "com.android.providers.media.documents" == uri.authority

    private fun isGooglePhotosUri(uri: Uri): Boolean =
        "com.google.android.apps.photos.content" == uri.authority
}