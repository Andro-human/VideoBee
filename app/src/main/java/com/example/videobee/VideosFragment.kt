package com.example.videobee

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videobee.MainActivity.Companion.videoList
import com.example.videobee.databinding.FragmentVideosBinding

class VideosFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_videos, container, false)
        val binding = FragmentVideosBinding.bind(view)

        binding.VideoRV.setHasFixedSize(true)           // makes it so it has a fixed size
        binding.VideoRV.setItemViewCacheSize(10)        // no. of items in cache at a time
        binding.VideoRV.layoutManager = LinearLayoutManager(requireContext())                  // passing the layout Inflator
        binding.VideoRV.adapter = VideoAdapter(requireContext(), MainActivity.videoList)
        binding.totalVideos.text = "Total Videos: ${videoList.size}"
        return view
    }

}