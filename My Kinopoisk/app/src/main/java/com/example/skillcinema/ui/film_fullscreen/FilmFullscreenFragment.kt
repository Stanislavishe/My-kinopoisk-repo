package com.example.skillcinema.ui.film_fullscreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.skillcinema.R
import com.example.skillcinema.data.models.SingleFilm
import com.example.skillcinema.databinding.FragmentFilmFullscreenBinding
import com.example.skillcinema.entity.InterestedMovies
import com.example.skillcinema.entity.MoviesViewed
import com.example.skillcinema.ui.actor_fillscreen.navigateSave
import com.example.skillcinema.ui.dialogs.FilmMenuDialog
import com.example.skillcinema.ui.dialogs.FilmMenuDialog.Companion.TAG
import com.example.skillcinema.ui.film_fullscreen.adapters.GalleryAdapter
import com.example.skillcinema.ui.film_fullscreen.adapters.SimilarFilmsAdapter
import com.example.skillcinema.ui.film_fullscreen.adapters.SmallStaffAdapter
import com.example.skillcinema.ui.films_list.FilmListFragment.Companion.SINGLEID
import com.example.skillcinema.ui.home.HomeFragment
import com.example.skillcinema.ui.home.HomeViewModel
import com.example.skillcinema.ui.home.HomeViewModelFactory
import com.example.skillcinema.ui.serch_settings.SearchSettingsFragment.Companion.TV_SERIES
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilmFullscreenFragment : Fragment() {
    @Inject
    lateinit var fullscreenViewModelFactory: FullscreenViewModelFactory

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private var _binding: FragmentFilmFullscreenBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by activityViewModels<HomeViewModel> { homeViewModelFactory }

    private val starredAdapter = SmallStaffAdapter { onStaffClick(it.staffId) }
    private val staffAdapter = SmallStaffAdapter { onStaffClick(it.staffId) }
    private val galleryAdapter = GalleryAdapter { onFrameClick(it.previewUrl) }
    private val similarFilms by lazy {
        SimilarFilmsAdapter(
            { onFilmClick(it.filmId) },
            homeViewModel,
            lifecycleScope
        )
    }

    private val fullscreenViewModel by activityViewModels<FullscreenViewModel> { fullscreenViewModelFactory }

    private var filmId: Int? = null

    private var need = true

    private var isLikeChecked = false
    private var isBookmarksChecked = false
    private var isViewedChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            filmId = it.getInt(SINGLEID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmFullscreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.swipe.setOnRefreshListener {
            fullscreenViewModel.refresh(filmId!!)
        }
        fullscreenViewModel.isLoading.onEach {
            binding.swipe.isRefreshing = it
        }.launchIn(lifecycleScope)

        fullscreenViewModel.loadMovie(filmId!!)
        fullscreenViewModel.movie.onEach { list ->
            if (list.isNotEmpty()) {
                val movie = list.last()
                startLoad(movie)

                with(binding) {
                    showDifferentFilms.setOnClickListener {
                        sendMarker(differentFilms.text.toString(), movie.kinopoiskId)
                    }
                    gallery.setOnClickListener {onClickGallery(movie) }
                    seasonPreparation(movie)
                    setAdapters()

                    observeViewed(movie)

                    loadFirstCollections(LIKED, R.drawable.like_icon_true, movie)
                    loadFirstCollections(WANT_WATCH, R.drawable.bookmarks_icon_true, movie)

                    setDescription(movie)
                    val descriptionFullText = description.text.toString()
                    setMaxSize(need, descriptionFullText)

                    description.setOnClickListener { need = !need; setMaxSize(need, descriptionFullText) }
                    poster.setOnClickListener { onFrameClick(movie.posterUrlPreview!!) }

                    showDialog(movie)
                    observeStaffs()
                    observeSimilar()

                    fullscreenViewModel.filmFrames(movie.kinopoiskId).onEach {
                        galleryAdapter.submitData(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)

                    bookmarksButton.setOnClickListener {
                        isBookmarksChecked = !isBookmarksChecked
                        checkedIcons(isBookmarksChecked, movie, WANT_WATCH)
                    }
                    alreadyViewed.setOnClickListener {
                        isViewedChecked = !isViewedChecked
                        actionForViewed(isViewedChecked, movie)
                    }
                    likeButton.setOnClickListener {
                        isLikeChecked = !isLikeChecked
                        checkedIcons(isLikeChecked, movie, LIKED)
                    }
                    shareButton.setOnClickListener { showShare(movie) }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeViewed(movie: SingleFilm){
        fullscreenViewModel.viewedFlow.onEach {
            if (it.isNotEmpty()) {
                it.forEach { id ->
                    if (movie.kinopoiskId == id) {
                        binding.alreadyViewed.setImageResource(R.drawable.viewed_icon)
                        isViewedChecked = true
                    }
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun onClickGallery(movie: SingleFilm){
        val bundle = Bundle().apply {
            putInt(GALERYID, movie.kinopoiskId)
        }
        findNavController().navigateSave(
            R.id.action_filmFullscreen_to_gallery,
            args = bundle
        )
    }

    private fun actionForViewed(checked: Boolean, movie: SingleFilm){
        val film = MoviesViewed(
            movie.kinopoiskId,
            movie.posterUrlPreview,
            movie.nameRu ?: movie.nameEn ?: movie.nameOriginal ?: "",
            movie.genres.joinToString(", ") { it.genre },
            movie.ratingKinopoisk
        )
        if (checked) {
            binding.alreadyViewed.setImageResource(R.drawable.viewed_icon)
            fullscreenViewModel.setViewed(film)
        } else {
            binding.alreadyViewed.setImageResource(R.drawable.not_viewed_icon)
            fullscreenViewModel.deleteViewed(film)
        }
    }

    private fun showShare(movie: SingleFilm){
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, movie.posterUrlPreview)
            type = "image/jpeg"
        }
        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

    private fun checkedIcons(checked: Boolean, movie: SingleFilm, marker: String){
        if(marker == LIKED) {
            if (checked) {
                fullscreenViewModel.setFilmForCollection(movie.kinopoiskId, LIKED)
                binding.likeButton.setImageResource(R.drawable.like_icon_true)
            } else {
                fullscreenViewModel.deleteFilmInCollection(movie.kinopoiskId, LIKED)
                binding.likeButton.setImageResource(R.drawable.like_icon)
            }
        } else {
            if (checked) {
                fullscreenViewModel.setFilmForCollection(movie.kinopoiskId, WANT_WATCH)
                binding.bookmarksButton.setImageResource(R.drawable.bookmarks_icon_true)
            } else {
                fullscreenViewModel.deleteFilmInCollection(movie.kinopoiskId, WANT_WATCH)
                binding.bookmarksButton.setImageResource(R.drawable.bookmarks_icon_false)
            }
        }

    }

    private fun observeSimilar(){
        with((binding)){
            fullscreenViewModel.similarFilm.onEach {
                val visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                differentFilms.visibility = visibility
                showDifferentFilms.visibility = visibility
                differentFilmsList.visibility = visibility
                similarFilms.submitList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }
    private fun observeStaffs() {
        fullscreenViewModel.staffsMap.onEach { map ->
            val actors = map["ACTOR"]
            val other = map["OTHER"]
            if (actors != null) {
                lifecycleScope.launch { starredAdapter.submitList(actors) }
            }
            if (other != null) {
                lifecycleScope.launch { staffAdapter.submitList(other) }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showDialog(movie: SingleFilm) {
        val bottomSheet = FilmMenuDialog()
        binding.additionalMenuButton.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable(FULL_FILM, movie)
            }
            bottomSheet.show(parentFragmentManager, TAG)
            bottomSheet.arguments = bundle
        }
    }

    private fun loadFirstCollections(marker: String, id: Int, movie: SingleFilm) {
        fullscreenViewModel.getMoviesCollection(marker)
        fullscreenViewModel.movieCollection.onEach {
            if (it.isNotEmpty()) {
                it.last().movie.forEach { mov ->
                    if (mov.id == movie.kinopoiskId) {
                        binding.bookmarksButton.setImageResource(id)
                        isBookmarksChecked = true
                        return@onEach
                    }
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun seasonPreparation(movie: SingleFilm) {
        with(binding) {
            if (movie.type == TV_SERIES) {
                seasons.visibility = View.VISIBLE
                seasons.setOnClickListener {
                    val bundle = Bundle().apply {
                        putInt(SERIALID, filmId!!)
                        putString(
                            SERIALNAME,
                            movie.nameRu ?: movie.nameEn ?: movie.nameOriginal ?: ""
                        )
                    }
                    findNavController().navigateSave(
                        R.id.action_filmFullscreen_to_seasonsSeriesList,
                        bundle
                    )
                }
            } else seasons.visibility = View.GONE
        }
    }

    private fun setAdapters() {
        with(binding) {
            starredInFilmList.adapter = starredAdapter
            workingOnFilmList.adapter = staffAdapter
            galleryList.adapter = galleryAdapter
            differentFilmsList.adapter = similarFilms
        }
    }

    private fun startLoad(movie: SingleFilm) {
        fullscreenViewModel.loadStaffs(movie.kinopoiskId)
        fullscreenViewModel.loadSimilarFilms(movie.kinopoiskId)
        fullscreenViewModel.getViewed()
        fullscreenViewModel.setInterested(
            InterestedMovies(
                movie.kinopoiskId,
                movie.posterUrlPreview,
                movie.nameRu ?: movie.nameEn ?: movie.nameOriginal
                ?: "Not name",
                movie.genres.joinToString(", ") { it.genre },
                movie.ratingKinopoisk
            )
        )
    }

    private fun onStaffClick(id: Int) {
        val bundle = Bundle().apply {
            putInt(ACTORID, id)
        }
        findNavController().navigate(R.id.action_filmFullscreen_to_staffFullscreen, args = bundle)
    }

    private fun onFrameClick(url: String) {
        val bundle = Bundle().apply {
            putString(IMAGEID, url)
        }
        findNavController().navigateSave(
            R.id.action_filmFullscreen_to_PhotoFullscreen,
            args = bundle
        )
    }

    private fun sendMarker(name: String, id: Int) {
        val bundle = Bundle().apply {
            putString(HomeFragment.NAME, name)
            putInt(HomeFragment.MARKER, HomeFragment.SIMILAR)
            putInt(FILMID, id)
        }
        findNavController().navigate(R.id.action_filmFullscreen_to_filmList, args = bundle)
    }

    private fun onFilmClick(id: Int) {
        fullscreenViewModel.loadMovie(id)
    }

    private fun setDescription(movie: SingleFilm) {
        Glide
            .with(binding.poster.context)
            .load(movie.posterUrlPreview)
            .into(binding.poster)

        val text = movie.nameRu ?: movie.nameEn ?: movie.nameOriginal ?: ""
        val ratingKinopoisk = movie.ratingKinopoisk ?: ""
        binding.ratingName.text = "$ratingKinopoisk, $text"
        binding.yearGenres.text =
            "${movie.year}, ${movie.genres.joinToString(", ") { it.genre }}"

        val length = movie.filmLength ?: 0
        val hours = length / 60
        val hoursText = if (length > 60) "${hours}ч " else ""
        val minutes = (length - hours * 60).toString()
        val countries = movie.countries.joinToString(", ") { it.country }
        val age = movie.ratingAgeLimits?.substring(3) ?: ""
        val ageText = ", $age+"
        binding.countryTimeAge.text =
            "$countries, $hoursText$minutes мин$ageText"

        val shortDescription = movie.shortDescription
        val descriptionText = movie.description
        if (shortDescription == null) {
            binding.description.text = descriptionText
        } else binding.description.text = "$shortDescription \n\n$descriptionText"
        if (binding.description.text == "") binding.description.visibility = View.GONE
    }

    private fun setMaxSize(need: Boolean, text: String) {
        if (need) {
            if (text.length > 250) {
                binding.description.text = text.take(247) + "..."
            } else binding.description.text = text
        } else binding.description.text = text
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        const val FILMID = "FILMID"
        const val ACTORID = "ACTORID"
        const val GALERYID = "GALERYID"
        const val SERIALID = "SERIALID"
        const val SERIALNAME = "SERIALNAME"
        const val IMAGEID = "IMAGEID"
        const val LIKED = "Любимые"
        const val WANT_WATCH = "Хочу посмотреть"
        const val FULL_FILM = "FULL_FILM"
    }

}