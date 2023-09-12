package com.example.videobee

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.videobee.databinding.FolderViewBinding
import com.example.videobee.databinding.VideoViewBinding

class FolderAdapter(private val context: Context, private var folderList: ArrayList<Folder>): RecyclerView.Adapter<FolderAdapter.MyHolder>() {
    class MyHolder(binding: FolderViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val folderName = binding.folderNameFV
        val root = binding.root
    }

    // called when a view is created
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(FolderViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }
    // called when the view is being initialized or attached to the screen
    override fun onBindViewHolder(holder:MyHolder, position: Int) {
        holder.folderName.text = folderList[position].folderName

            //to move to folder activity
        holder.root.setOnClickListener {
            val intent = Intent(context, FolderActivity::class.java)
            intent.putExtra("position", position)               // to pass data with intent
            ContextCompat.startActivity(context, intent, null)                                                // since it is an adapter we cannot directly pass intent without ContextCompat
        }
    }

    //  gives total items
    override fun getItemCount(): Int {
        return folderList.size
    }
}