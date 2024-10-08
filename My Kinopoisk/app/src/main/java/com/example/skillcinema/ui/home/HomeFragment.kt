package com.example.skillcinema.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentHomeBinding
import com.example.skillcinema.ui.actor_fillscreen.navigateSave
import com.example.skillcinema.ui.dialogs.ErrorDialog
import com.example.skillcinema.ui.films_list.FilmListFragment.Companion.SINGLEID
import com.example.skillcinema.ui.hello_screens.FirstFragment
import com.example.skillcinema.ui.hello_screens.SecondFragment
import com.example.skillcinema.ui.hello_screens.ThirdFragment
import com.example.skillcinema.ui.hello_screens.HelloScreensAdapter
import com.example.skillcinema.ui.home.adapters.PopularsAdapter
import com.example.skillcinema.ui.home.adapters.PremiersAdapter
import com.example.skillcinema.ui.home.adapters.SeriesRandomSelectionAdapter
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by activityViewModels<HomeViewModel> { homeViewModelFactory }

    private val premiersAdapter by lazy {
        PremiersAdapter({ onFilmClick(it.kinopoiskId) }, homeViewModel, lifecycleScope)
    }
    private val popularsFilms by lazy {
        PopularsAdapter({ onFilmClick(it.kinopoiskId) }, homeViewModel, lifecycleScope)
    }
    private val top250Adapter by lazy {
        PopularsAdapter({ onFilmClick(it.kinopoiskId) }, homeViewModel, lifecycleScope)
    }
    private val serialsAdapter by lazy {
        SeriesRandomSelectionAdapter(
            { onFilmClick(it.kinopoiskId) },
            homeViewModel,
            lifecycleScope
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getViewed()
        homeViewModel.loadPremiers()
        with(binding) {
            setAdapters()

            scrollView.setOnRefreshListener {
                refresh()
            }
            homeViewModel.isLoading.onEach {
                Log.d("HomeFragment", it.toString())
                scrollView.isRefreshing = it
            }.launchIn(lifecycleScope)

            popularsFilms.loadStateFlow.onEach {
                if(it.hasError){
                    ErrorDialog().show(parentFragmentManager, ErrorDialog.TAG)
                }
                scrollView.isRefreshing = it.refresh == LoadState.Loading
            }.launchIn(lifecycleScope)

            top250Adapter.loadStateFlow.onEach {
                scrollView.isRefreshing = it.refresh == LoadState.Loading
            }.launchIn(lifecycleScope)

            serialsAdapter.loadStateFlow.onEach {
                scrollView.isRefreshing = it.refresh == LoadState.Loading
            }.launchIn(lifecycleScope)

            allPremiers.setOnClickListener {
                sendMarker(PREMIERS, premiersText.text.toString())
            }
            allPopular.setOnClickListener {
                sendMarker(POPULAR, popularText.text.toString())
            }
            allTop250.setOnClickListener {
                sendMarker(TOP, top250Text.text.toString())
            }
            allSerials.setOnClickListener {
                sendMarker(SERIALS, serialsText.text.toString())
            }
        }

        with(homeViewModel) {
            premiers.onEach {
                premiersAdapter.submitList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            popularsMovies.onEach {
                popularsFilms.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            top250.onEach {
                top250Adapter.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            serials.onEach {
                serialsAdapter.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun refresh(){
        homeViewModel.getViewed()
        homeViewModel.loadPremiers()
        popularsFilms.refresh()
        top250Adapter.refresh()
        serialsAdapter.refresh()
    }

    private fun setAdapters(){
        with(binding) {
            premiersList.adapter = premiersAdapter
            popularList.adapter = popularsFilms
            top250List.adapter = top250Adapter
            serialsList.adapter = serialsAdapter
        }
    }

    private fun sendMarker(marker: Int, name: String) {
        val bundle = Bundle().apply {
            putInt(MARKER, marker)
            putString(NAME, name)
        }
        findNavController().navigateSave(
            R.id.action_navigation_home_to_fragment_film_list,
            args = bundle
        )
    }

    private fun onFilmClick(id: Int) {
        val bundle = Bundle().apply {
            putInt(SINGLEID, id)
        }
        findNavController().navigateSave(
            R.id.action_navigation_home_to_film_fullscreen,
            args = bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val MARKER = "MARKER"
        const val NAME = "NAME"
        const val PREMIERS = 0
        const val POPULAR = 1
        const val TOP = 2
        const val SERIALS = 3
        const val SIMILAR = 4
    }
}