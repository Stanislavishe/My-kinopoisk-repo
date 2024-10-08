package com.example.skillcinema.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.filter
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentSearchBinding
import com.example.skillcinema.ui.actor_fillscreen.navigateSave
import com.example.skillcinema.ui.dialogs.ErrorDialog
import com.example.skillcinema.ui.films_list.FilmListFragment.Companion.SINGLEID
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Inject
    lateinit var searchViewModelFactory: SearchViewModelFactory

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels { searchViewModelFactory }

    private val adapter = SearchAdapter { onFilmClick(it.kinopoiskId) }
    private var viewedListIds = listOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchList.adapter = adapter

        viewModel.getViewed()
        viewModel.viewedFlow.onEach {
            viewedListIds = it
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        searchMovies(null)

        binding.searchEdit.doOnTextChanged { text, _, _, _ ->
            searchMovies(text)
        }
        binding.filterIcon.setOnClickListener {
            findNavController().navigateSave(R.id.action_search_to_searchSettingsFragment)
        }
        binding.swipe.setOnRefreshListener {
            adapter.refresh()
        }
        adapter.loadStateFlow.onEach {
            if(it.hasError){
                ErrorDialog().show(parentFragmentManager, ErrorDialog.TAG)
            }
            binding.swipe.isRefreshing = it.refresh == LoadState.Loading
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun searchMovies(text: CharSequence?){
        viewModel.searchMovies(text).onEach {
            viewModel.showViewed.onEach { show ->
                if(show){
                    adapter.submitData(it)
                } else {
                    adapter.submitData( it.filter { !viewedListIds.contains(it.kinopoiskId)})
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onFilmClick(id: Int) {
        val bundle = Bundle().apply {
            putInt(SINGLEID, id)
        }
        findNavController().navigateSave(R.id.action_search_to_filmFullscreen, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}