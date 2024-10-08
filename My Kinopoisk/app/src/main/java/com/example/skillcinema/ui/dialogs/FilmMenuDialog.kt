package com.example.skillcinema.ui.dialogs

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.skillcinema.R
import com.example.skillcinema.data.models.SingleFilm
import com.example.skillcinema.databinding.FragmentFilmMenuDialogBinding
import com.example.skillcinema.entity.FilmOrSerial
import com.example.skillcinema.ui.film_fullscreen.FilmFullscreenFragment.Companion.FULL_FILM
import com.example.skillcinema.ui.profile.ProfileViewModel
import com.example.skillcinema.ui.profile.ProfileViewModelFactory
import com.example.skillcinema.ui.profile.adapters.CollectionsListAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilmMenuDialog : BottomSheetDialogFragment() {

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val viewModel by activityViewModels<ProfileViewModel> { profileViewModelFactory }

    private var _binding: FragmentFilmMenuDialogBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { CollectionsListAdapter(viewModel) }

    private var url: String? = null
    private var ratingFilm: Double? = null
    private var name: String? = null
    private var genre: String? = null
    private var movie: SingleFilm? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = it.getParcelable(FULL_FILM, SingleFilm::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmMenuDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            collections.adapter = adapter
            setDescr()

            addBtn.setOnClickListener {
                val dialog = CreateCollectionDialog()
                dialog.show(parentFragmentManager, CreateCollectionDialog.TAG)
            }
        }
        actionFromCollections(INSERT)
        actionFromCollections(DELETE)

        viewModel.collections.onEach {
            adapter.submitList(it)
        }.launchIn(lifecycleScope)
    }

    private fun actionFromCollections(marker: String){
        val flow = if (marker == INSERT) viewModel.addInCollection else viewModel.deleteOnCollection
        val film = FilmOrSerial(
            id = movie?.kinopoiskId!!,
            url = url!!,
            name = name!!,
            genre = genre!!,
            rating = ratingFilm
        )
        lifecycleScope.launch(Dispatchers.IO) {
            flow.collect{
                if (marker == INSERT) viewModel.setFilmForCollection(film, it)
                else viewModel.deleteFilmFromCollection(film, it)
            }
        }

    }

    private fun setDescr(){
        with(binding){
            name = movie?.nameRu ?: movie?.nameEn ?: movie?.nameOriginal ?: ""
            genre = movie?.genres?.joinToString(", ") { it.genre }
            ratingFilm = movie?.ratingKinopoisk
            url = movie?.posterUrlPreview
            filmName.text = name
            filmYear.text = genre
            Glide.with(filmImage.context).load(url).into(filmImage)
            if (ratingFilm == null) rating.visibility = View.GONE
            else rating.text = ratingFilm.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "FilmMenuDialog"
        private const val DELETE = "DELETE"
        private const val INSERT = "INSERT"
    }
}