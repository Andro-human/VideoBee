package com.example.videobee

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.videobee.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    // late initialization
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle      // for working with navigation drawer
    // tells where we want the videos fetched from the cursor
    companion object{
        lateinit var videoList: ArrayList<Video>
        lateinit var folderList: ArrayList<Folder>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)                        //Sets the main Theme
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)       // initialization
        setContentView(binding.root)

        // Check the system-wide auto-rotate setting
        val isAutoRotateEnabled = android.provider.Settings.System.getInt(
            contentResolver,
            android.provider.Settings.System.ACCELEROMETER_ROTATION, 0
        ) == 1

        // Conditionally set the screen orientation based on the auto-rotate setting
        if (isAutoRotateEnabled) {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        } else {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED // Lock to portrait
        }

        //for Nav Drawer
        toggle = ActionBarDrawerToggle(this, binding.root, R.string.open, R.string.close)       // sets toggle var as an ActionBarDrawerToggle
        binding.root.addDrawerListener(toggle)
        toggle.syncState()                                                      // synchronize the state of a toggle with the current state of the navigation drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)               //  enable up and back button

        if (requestRuntimePermission()) {
            folderList = ArrayList()                        // initialization
            videoList = getAllVideos(this)
            setFragment(VideosFragment())                                   // sets a default fragment
        }
        else {
            folderList = ArrayList()
            videoList = ArrayList()
        }

        //for fragments (All videos/ All folders)
        binding.bottomNav.setOnItemSelectedListener {               // change fragment
            when(it.itemId) {
                R.id.videoView -> setFragment(VideosFragment())
                R.id.foldersView -> setFragment(FoldersFragment())
            }
            return@setOnItemSelectedListener true
        }
        // for nav menu items
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.aboutNav -> startActivity(Intent(this, AboutActivity::class.java))
                R.id.exitNav -> exitProcess(1)
            }
            return@setNavigationItemSelectedListener true
        }
    }

    // function to change fragment
    private fun setFragment(fragment:Fragment) {
        val transaction = supportFragmentManager.beginTransaction()      // to start the fragment change
        transaction.replace(R.id.fragmentFL, fragment)
        transaction.disallowAddToBackStack()                // no frame fragment in background (saves background memory)
        transaction.commit()
    }

    // for requesting permission
    private fun requestRuntimePermission(): Boolean{
        //android 13 permission request
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_VIDEO)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_VIDEO), 7)
                return false
            }
            return true
        }
        //requesting storage permission for devices less than api 33
        else {
            if (ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 7)
                return false
            }
        }
        return true
    }

    // what happens when permission is allowed
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 7) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                folderList = ArrayList()
                videoList = getAllVideos(this)
                setFragment(VideosFragment())
            }
            else
                Snackbar.make(binding.root, "Storage Permission Needed!!", 5000)
                    .setAction("OK"){
                        ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE),7)
                    }
                    .show()
//                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 7)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }
}