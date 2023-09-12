package com.example.videobee

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videobee.MainActivity.Companion.folderList
import com.example.videobee.databinding.FragmentFoldersBinding
import com.example.videobee.databinding.FragmentVideosBinding


class FoldersFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_folders, container, false)
        val binding = FragmentFoldersBinding.bind(view)
        binding.FolderRV.setHasFixedSize(true)           // makes it so it has a fixed size
        binding.FolderRV.setItemViewCacheSize(10)        // no. of items in cache at a time
        binding.FolderRV.layoutManager = LinearLayoutManager(requireContext())                  // passing the layout Inflator
        binding.FolderRV.adapter = FolderAdapter(requireContext(), MainActivity.folderList)
        binding.totalFolders.text = "Total Folders: ${folderList.size}"
        return view
    }

}