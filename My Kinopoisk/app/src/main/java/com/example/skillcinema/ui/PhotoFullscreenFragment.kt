package com.example.skillcinema.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentPhotoFullscreenBinding
import com.example.skillcinema.ui.film_fullscreen.FilmFullscreenFragment.Companion.IMAGEID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoFullscreenFragment : Fragment() {

    private var _binding: FragmentPhotoFullscreenBinding? = null
    private val binding get() = _binding!!

    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = it.getString(IMAGEID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoFullscreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        Glide
            .with(binding.photo.context)
            .load(imageUrl)
            .into(binding.photo)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}