package com.example.videobee

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.videobee.MainActivity.Companion.folderList
import java.io.File
import java.lang.Exception

data class Video(val id: String, val title: String, val duration: Long = 0, val folderName: String, val size: String
, val path: String, val atUri: Uri)

data class Folder(val id:String, val folderName:String)

//     cursor through which data can be accessed for recyclerViews
@SuppressLint("Recycle", "Range")
fun getAllVideos(context: Context): ArrayList<Video>{
    val tempList = ArrayList<Video>()
    val tempFolderList = ArrayList<String>()
    val projection = arrayOf(
        MediaStore.Video.Media.TITLE, MediaStore.Video.Media.SIZE, MediaStore.Video.Media._ID,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_ADDED,
        MediaStore.Video.Media.DURATION, MediaStore.Video.Media.BUCKET_ID)
    // cursor
    val cursor = context.contentResolver.query(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null,
        MediaStore.Video.Media.DATE_ADDED + " DESC")
    if(cursor != null) {
        if (cursor.moveToNext())
            do {
                val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))?:"Unknown"
                val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))?:"Unknown"
                val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))?:"Unknown"
                val folderIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))?:"Unknown"
                val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))?:"Unknown"
                val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))?:"Unknown"
                val durationC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))?.toLong()?:0L

                // now we need to initialize the video object with all this data
                try {
                    val file = File(pathC)
                    val atUriC = Uri.fromFile(file)
                    val video = Video(
                        title = titleC, id = idC, folderName = folderC, duration = durationC,
                        size = sizeC, path = pathC, atUri = atUriC
                    )
                    if (file.exists())
                        tempList.add(video)

                    // for adding folders
                    if(!tempFolderList.contains(folderC)) {
                        tempFolderList.add(folderC)
                        folderList.add(Folder(id = folderIdC, folderName = folderC))
                    }


                } catch (_: Exception) {
                }
            } while (cursor.moveToNext())
    }
        cursor?.close()
    return tempList
}