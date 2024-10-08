package com.example.skillcinema.ui.serch_settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.data.models.search.GenreId
import com.example.skillcinema.databinding.FragmentCatchCountryBinding
import com.example.skillcinema.ui.actor_fillscreen.navigateSave
import com.example.skillcinema.ui.search.SearchViewModel
import com.example.skillcinema.ui.search.SearchViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatchGenreFragment : Fragment() {

    @Inject
    lateinit var searchViewModelFactory: SearchViewModelFactory
    private var _binding: FragmentCatchCountryBinding? = null
    private val binding get() = _binding!!

    private val adapter = CountryGenreAdapter { onGenreClick(it as GenreId) }

    private val searchViewModel: SearchViewModel by activityViewModels { searchViewModelFactory }
    private val searchSettingsViewModel: SearchSettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatchCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listName.text = getString(R.string.genre)
        binding.itemList.adapter = adapter
        searchViewModel.loadGenres()

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        searchViewModel.genreList.onEach {
            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val listValues = mutableListOf<String>()
                    it.forEach { genre ->
                        listValues.add(genre.genre)
                    }
                    if (listValues.contains(query)) {
                        lifecycleScope.launch {
                            adapter.submitList(it.filter { it.genre == query })
                        }
                    }
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    lifecycleScope.launch {
                        if (newText != null)
                            adapter.submitList(it.filter { it.genre.contains(newText) })
                    }
                    return false
                }
            })
            adapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onGenreClick(genre: GenreId) {
        searchSettingsViewModel.sendGenre(genre)
        findNavController().navigateSave(
            R.id.action_catchGenreFragment_to_searchSettingsFragment
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}