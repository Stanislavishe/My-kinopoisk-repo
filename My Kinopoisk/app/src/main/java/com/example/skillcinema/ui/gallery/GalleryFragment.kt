package com.example.skillcinema.ui.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentGalleryBinding
import com.example.skillcinema.ui.actor_fillscreen.navigateSave
import com.example.skillcinema.ui.film_fullscreen.FilmFullscreenFragment.Companion.GALERYID
import com.example.skillcinema.ui.film_fullscreen.FilmFullscreenFragment.Companion.IMAGEID
import com.example.skillcinema.ui.film_fullscreen.adapters.GalleryAdapter
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    @Inject
    lateinit var galleryViewModelFactory: GalleryViewModelFactory

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val galleryViewModel: GalleryViewModel by viewModels{ galleryViewModelFactory }

    private val galleryAdapter = GalleryAdapter { onFrameClick(it.previewUrl) }

    private var filmId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {filmId = it.getInt(GALERYID)}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            backButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            imagesRecycler.adapter = galleryAdapter
            setImages(true, STILL)
            stillChip.setOnCheckedChangeListener { _, isChecked ->
                setImages(isChecked, STILL)
            }
            shootingChip.setOnCheckedChangeListener { _, isChecked ->
                setImages(isChecked, SHOOTING)
            }
            postersChip.setOnCheckedChangeListener { _, isChecked ->
                setImages(isChecked, POSTER)
            }
        }
    }

    private fun setImages(isChecked: Boolean, name: String) {
        if (isChecked){
            galleryViewModel.filmFrames(name, filmId!!).onEach {
                galleryAdapter.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun onFrameClick(url: String) {
        val bundle = Bundle().apply {
            putString(IMAGEID, url)
        }
        findNavController().navigateSave(R.id.action_gallery_to_photoFullscreen, args = bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val STILL = "STILL"
        private const val SHOOTING = "SHOOTING"
        private const val POSTER = "POSTER"
    }
}