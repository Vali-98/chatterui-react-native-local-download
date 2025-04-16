package com.chatterui.reactnativelocaldownload

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.Promise
import com.facebook.react.module.annotations.ReactModule

import android.content.ContentValues
import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URLConnection

@ReactModule(name = ReactNativeLocalDownloadModule.NAME)
class ReactNativeLocalDownloadModule(private val reactContext: ReactApplicationContext) :
  NativeReactNativeLocalDownloadSpec(reactContext) {

  override fun getName(): String {
    return NAME
  }

  override fun localDownload(uri: String, promise: Promise) {
    try {
      val inputFile = File(uri)
      if (!inputFile.exists()) {
        promise.reject("FILE_NOT_FOUND", "File does not exist at path: $uri")
        return
      }
  
      val fileName = inputFile.name
      val mimeType = URLConnection.guessContentTypeFromName(inputFile.name) ?: "application/octet-stream"
      val resolver : ContentResolver = reactContext.contentResolver
      val uniqueName = getUniqueFileName(resolver, fileName)
      val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, uniqueName)
        put(MediaStore.Downloads.MIME_TYPE, mimeType)
        put(MediaStore.Downloads.IS_PENDING, 1)
      }
  
      val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
      val itemUri = resolver.insert(collection, contentValues)
  
      if (itemUri == null) {
        promise.reject("SAVE_ERROR", "Failed to create destination file in MediaStore.")
        return
      }
  
      resolver.openOutputStream(itemUri)?.use { outputStream ->
        inputFile.inputStream().use { inputStream ->
          inputStream.copyTo(outputStream)
        }
      }
  
      // Mark the item as not pending so it's visible to user
      contentValues.clear()
      contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
      resolver.update(itemUri, contentValues, null, null)
  
      promise.resolve(itemUri.toString())
    } catch (e: Exception) {
      promise.reject("DOWNLOAD_ERROR", "Failed to save file via MediaStore: ${e.message}", e)
    }
  }
  
  private fun getUniqueFileName(resolver: ContentResolver, baseName: String): String {
    var name = baseName
    val nameWithoutExtension = File(baseName).nameWithoutExtension
    val extension = File(baseName).extension
    var index = 1
  
    val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
  
    val projection = arrayOf(MediaStore.Downloads.DISPLAY_NAME)
    val selection = "${MediaStore.Downloads.DISPLAY_NAME} = ?"
    val selectionArgs = arrayOf(name)
  
    while (resolver.query(collection, projection, selection, selectionArgs, null)?.use { it.moveToFirst() } == true) {
      name = if (extension.isNotEmpty()) {
        "$nameWithoutExtension ($index).$extension"
      } else {
        "$nameWithoutExtension ($index)"
      }
      selectionArgs[0] = name
      index++
    }
  
    return name
  }

  companion object {
    const val NAME = "ReactNativeLocalDownload"
  }
}
