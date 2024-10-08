package com.example.skillcinema.ui.seasons_series_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.skillcinema.data.models.seasonsList.Season
import com.example.skillcinema.databinding.FragmentSeasonsSeriesListBinding
import com.example.skillcinema.ui.film_fullscreen.FilmFullscreenFragment.Companion.SERIALID
import com.example.skillcinema.ui.film_fullscreen.FilmFullscreenFragment.Companion.SERIALNAME
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SeasonsSeriesListFragment : Fragment() {

    @Inject
    lateinit var seasonsSeriesViewModelFactory: SeasonsSeriesViewModelFactory
    private var _binding: FragmentSeasonsSeriesListBinding? = null
    private val binding get() = _binding!!

    private var serialId: Int? = null
    private var serialName: String? = null

    private val viewModel: SeasonsSeriesViewModel by viewModels { seasonsSeriesViewModelFactory }

    private val adapter = SeasonsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serialId = it.getInt(SERIALID)
            serialName = it.getString(SERIALNAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeasonsSeriesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            backButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            seriesList.adapter = adapter
            listName.text = serialName
            swipe.setOnRefreshListener {
                viewModel.loadSeasons(serialId!!)
            }
            viewModel.isLoading.onEach {
                swipe.isRefreshing = it
            }.launchIn(lifecycleScope)
            viewModel.loadSeasons(serialId!!)
            viewModel.seasonsFlow.onEach { seasons ->
                if (seasons.isNotEmpty()) {
                    lifecycleScope.launch {
                        adapter.submitList(seasons[0].episodes)
                    }
                    seasons.forEachIndexed { index, _ ->
                        chipsVisibility(index)
                    }
                    chipsSeasons.setOnCheckedStateChangeListener { group, _ ->
                        seasonWithChip(group, seasons)
                    }
                }

            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun chipsVisibility(index: Int){
        with(binding){
            when (index) {
                0 -> firstChip.visibility = View.VISIBLE
                1 -> secondChip.visibility = View.VISIBLE
                2 -> thirdChip.visibility = View.VISIBLE
                3 -> forthChip.visibility = View.VISIBLE
                4 -> fifthChip.visibility = View.VISIBLE
            }
        }
    }

    private fun seasonWithChip(group: ChipGroup, seasons: List<Season>){
        val viewId = group.checkedChipId
        group.forEachIndexed { index, view ->
            if (view.id == viewId) {
                lifecycleScope.launch {
                    adapter.submitList(seasons[index].episodes)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}