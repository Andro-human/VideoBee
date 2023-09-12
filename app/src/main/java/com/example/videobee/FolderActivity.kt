package com.example.videobee

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videobee.databinding.ActivityFolderBinding
import java.io.File
import java.lang.Exception

class FolderActivity : AppCompatActivity() {

    companion object{
        lateinit var  currentFolderVideos: ArrayList<Video>
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFolderBinding.inflate(this.layoutInflater)
        setTheme(R.style.AppTheme)
        setContentView(/* view = */ binding.root)
        // handing the intent
        val position = intent.getIntExtra("position", 0)
        currentFolderVideos = getAllVideos(MainActivity.folderList[position].id)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = MainActivity.folderList[position].folderName

        val tempList = ArrayList<Video>()
        tempList.add(MainActivity.videoList[0])
        tempList.add(MainActivity.videoList[1])
        tempList.add(MainActivity.videoList[2])

        // initializing the recycler view
        binding.VideoRVFA.setHasFixedSize(true)           // makes it so it has a fixed size
        binding.VideoRVFA.setItemViewCacheSize(10)        // no. of items in cache at a time
        binding.VideoRVFA.layoutManager = LinearLayoutManager(this@FolderActivity)                  // passing the layout Inflator
        binding.VideoRVFA.adapter = VideoAdapter(this@FolderActivity, currentFolderVideos, true)
        binding.totalVideosFA.text = "Total Videos: ${currentFolderVideos.size}"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    //     cursor through which data can be accessed for recyclerViews of particular folder
    @SuppressLint("Range")
    fun getAllVideos(folderId: String): ArrayList<Video>{
        val tempList = ArrayList<Video>()
        val selection = MediaStore.Video.Media.BUCKET_ID + " like? "
        val projection = arrayOf(
            MediaStore.Video.Media.TITLE, MediaStore.Video.Media.SIZE, MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION, MediaStore.Video.Media.BUCKET_ID)
        // cursor
        val cursor = this.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, arrayOf(folderId),
            MediaStore.Video.Media.DATE_ADDED + " DESC")
        if(cursor != null) {
            if (cursor.moveToNext())
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))?:"Unknown"
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))?:"Unknown"
                    val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))?:"Unknown"
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

                    } catch (_: Exception) {
                    }
                } while (cursor.moveToNext())
        }
        cursor?.close()
        return tempList
    }
}