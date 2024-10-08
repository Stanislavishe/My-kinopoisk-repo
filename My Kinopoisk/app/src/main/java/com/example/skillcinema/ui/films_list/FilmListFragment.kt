package com.example.skillcinema.ui.films_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentFilmListBinding
import com.example.skillcinema.ui.SmallFilmViewHolder
import com.example.skillcinema.ui.actor_fillscreen.navigateSave
import com.example.skillcinema.ui.dialogs.ErrorDialog
import com.example.skillcinema.ui.film_fullscreen.FilmFullscreenFragment
import com.example.skillcinema.ui.film_fullscreen.adapters.SimilarFilmsAdapter
import com.example.skillcinema.ui.home.HomeFragment
import com.example.skillcinema.ui.home.HomeViewModel
import com.example.skillcinema.ui.home.HomeViewModelFactory
import com.example.skillcinema.ui.home.adapters.PopularsAdapter
import com.example.skillcinema.ui.home.adapters.PremiersAdapter
import com.example.skillcinema.ui.home.adapters.SeriesRandomSelectionAdapter
import com.example.skillcinema.ui.profile.ProfileFragment
import com.example.skillcinema.ui.profile.adapters.CollectionMoviesAdapter
import com.example.skillcinema.ui.profile.adapters.InterestedAdapter
import com.example.skillcinema.ui.profile.adapters.ViewedAdapter
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilmListFragment : Fragment() {

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private var _binding: FragmentFilmListBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by activityViewModels<HomeViewModel> { homeViewModelFactory }

    private var listName: String? = null
    private var marker: Int? = null
    private var id: Int? = null

    lateinit var adapter: RecyclerView.Adapter<SmallFilmViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listName = it.getString(HomeFragment.NAME)
            marker = it.getInt(HomeFragment.MARKER)
            id = it.getInt(FilmFullscreenFragment.FILMID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listName.text = listName
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        homeViewModel.getViewed()
        lifecycleScope.launch {
            setAdapter()

            binding.swipe.setOnRefreshListener {
                refresh()
            }
            homeViewModel.isLoading.onEach {
                binding.swipe.isRefreshing = it
            }.launchIn(lifecycleScope)
            homeViewModel.isError.onEach {
                if(it){ ErrorDialog().show(parentFragmentManager, ErrorDialog.TAG) }
            }.launchIn(lifecycleScope)

            when (marker) {
                HomeFragment.PREMIERS -> homeViewModel.premiers.onEach {
                    homeViewModel.loadPremiers()
                    (adapter as PremiersAdapter).submitList(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                HomeFragment.POPULAR -> homeViewModel.popularsMovies
                    .onEach {
                        (adapter as PopularsAdapter).loadStateFlow.onEach {
                            checkRefresh(it)
                        }.launchIn(lifecycleScope)
                        (adapter as PopularsAdapter).submitData(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)

                HomeFragment.TOP -> homeViewModel.top250
                    .onEach {
                        (adapter as PopularsAdapter).loadStateFlow.onEach {
                            checkRefresh(it)
                        }.launchIn(lifecycleScope)
                        (adapter as PopularsAdapter).submitData(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)

                HomeFragment.SERIALS -> homeViewModel.serials.onEach {
                    (adapter as SeriesRandomSelectionAdapter).loadStateFlow.onEach {
                        checkRefresh(it)
                    }.launchIn(lifecycleScope)
                    (adapter as SeriesRandomSelectionAdapter).submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                HomeFragment.SIMILAR -> {
                    homeViewModel.loadSimilarFilms(id!!)
                    homeViewModel.similarFilm.onEach {
                        (adapter as SimilarFilmsAdapter).submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                ProfileFragment.VIEWED -> {
                    homeViewModel.getViewed()
                    homeViewModel.viewedListFlow.onEach {
                        (adapter as ViewedAdapter).setData(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                ProfileFragment.INTERESTED -> {
                    homeViewModel.getInterested()
                    homeViewModel.interestedFlow.onEach {
                        (adapter as InterestedAdapter).setData(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                else -> {
                    homeViewModel.getMoviesCollection(listName!!)
                    homeViewModel.movieCollection.onEach {
                        if(it.isNotEmpty()){
                            (adapter as CollectionMoviesAdapter).setData(it.last().movie)
                        }
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
            }
        }
    }

    private fun checkRefresh(it: CombinedLoadStates){
        if(it.hasError){
            ErrorDialog().show(parentFragmentManager, ErrorDialog.TAG)
        }
        binding.swipe.isRefreshing = it.refresh == LoadState.Loading
    }

    private fun refresh(){
        homeViewModel.getViewed()
        when(marker){
            HomeFragment.PREMIERS -> homeViewModel.loadPremiers()
            HomeFragment.POPULAR -> (adapter as PopularsAdapter).refresh()
            HomeFragment.TOP -> (adapter as PopularsAdapter).refresh()
            HomeFragment.SERIALS -> (adapter as SeriesRandomSelectionAdapter).refresh()
            HomeFragment.SIMILAR -> homeViewModel.loadSimilarFilms(id!!)
            ProfileFragment.VIEWED -> homeViewModel.getViewed()
            ProfileFragment.INTERESTED -> homeViewModel.getInterested()
            else -> homeViewModel.getMoviesCollection(listName!!)
        }
    }

    private fun setAdapter(){
        adapter = when (marker) {
            HomeFragment.PREMIERS -> PremiersAdapter(
                { onFilmClick(it.kinopoiskId) },
                homeViewModel,
                lifecycleScope
            )
            HomeFragment.POPULAR -> PopularsAdapter(
                { onFilmClick(it.kinopoiskId) },
                homeViewModel,
                lifecycleScope
            )
            HomeFragment.TOP -> PopularsAdapter(
                { onFilmClick(it.kinopoiskId) },
                homeViewModel,
                lifecycleScope
            )
            HomeFragment.SERIALS -> SeriesRandomSelectionAdapter(
                { onFilmClick(it.kinopoiskId) },
                homeViewModel,
                lifecycleScope
            )
            HomeFragment.SIMILAR -> SimilarFilmsAdapter(
                { onFilmClick(it.filmId) },
                homeViewModel,
                lifecycleScope
            )
            ProfileFragment.VIEWED -> ViewedAdapter { onFilmClick(it.id) }
            ProfileFragment.INTERESTED -> InterestedAdapter { onFilmClick(it.id) }
            else -> CollectionMoviesAdapter {onFilmClick(it.id)}
        }
        binding.filmsList.adapter = adapter
    }

    private fun onFilmClick(id: Int) {
        val bundle = Bundle().apply {
            putInt(SINGLEID, id)
        }
        findNavController().navigate(R.id.action_filmList_to_filmFullscreen, args = bundle)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        const val SINGLEID = "SINGLEID"
    }
}