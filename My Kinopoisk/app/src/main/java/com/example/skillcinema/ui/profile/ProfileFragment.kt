package com.example.skillcinema.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentProfileBinding
import com.example.skillcinema.entity.Collection
import com.example.skillcinema.ui.actor_fillscreen.navigateSave
import com.example.skillcinema.ui.dialogs.CreateCollectionDialog
import com.example.skillcinema.ui.film_fullscreen.FilmFullscreenFragment
import com.example.skillcinema.ui.films_list.FilmListFragment.Companion.SINGLEID
import com.example.skillcinema.ui.home.HomeFragment.Companion.MARKER
import com.example.skillcinema.ui.home.HomeFragment.Companion.NAME
import com.example.skillcinema.ui.profile.adapters.CollectionsAdapter
import com.example.skillcinema.ui.profile.adapters.InterestedAdapter
import com.example.skillcinema.ui.profile.adapters.ViewedAdapter
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<ProfileViewModel> { profileViewModelFactory }

    private val viewedAdapter = ViewedAdapter { onFilmClick(it.id) }
    private val interestedAdapter = InterestedAdapter { onFilmClick(it.id) }
    private val collectionAdapter by lazy {
        CollectionsAdapter  { onCollectionClick(it.name) }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getViewed()
        viewModel.getInterested()

        with(binding) {
            setAdapters()

            swipe.setOnRefreshListener {
                viewModel.refresh()
            }
            viewModel.isLoading.onEach {
                swipe.isRefreshing = it
            }.launchIn(lifecycleScope)
            interestText.setOnClickListener {
                sendMarker(INTERESTED, interestText.text.toString())
            }
            premiersText.setOnClickListener {
                sendMarker(VIEWED, premiersText.text.toString())
            }
            createCollection.setOnClickListener {
                val dialog = CreateCollectionDialog()
                dialog.show(parentFragmentManager, CreateCollectionDialog.TAG)
            }
        }
        viewModel.interestedFlow.onEach {
            binding.allInterest.text = it.size.toString()
            interestedAdapter.setData(it)
        }.launchIn(lifecycleScope)

        viewModel.viewedFlow.onEach {
            binding.allPremiers.text = it.size.toString()
            viewedAdapter.setData(it)
        }.launchIn(lifecycleScope)

        viewModel.collections.onEach {
            loadFirstCollections(it)
            collectionAdapter.submitList(it)
        }.launchIn(lifecycleScope)
    }

    private fun setAdapters(){
        with(binding){
            premiersList.adapter = viewedAdapter
            interestList.adapter = interestedAdapter
            collectionsRecycler.adapter = collectionAdapter
        }
    }

    private fun loadFirstCollections(it: List<Collection>){
        if (it.isEmpty()) {
            try {
                viewModel.setCollection(
                    Collection(FilmFullscreenFragment.LIKED, R.drawable.heart_icon)
                )
                viewModel.setCollection(
                    Collection(FilmFullscreenFragment.WANT_WATCH, R.drawable.bookmark_icon)
                )
            } catch (e: Exception){
                Log.d("ProfileFragment", "onViewCreated: $e")
            }
        }
    }

    private fun onFilmClick(id: Int) {
        val bundle = Bundle().apply {
            putInt(SINGLEID, id)
        }
        findNavController().navigateSave(R.id.action_profile_to_filmFullscreen, bundle)
    }

    private fun sendMarker(marker: Int, name: String) {
        val bundle = Bundle().apply {
            putInt(MARKER, marker)
            putString(NAME, name)
        }
        findNavController().navigateSave(
            R.id.action_profile_to_filmList,
            args = bundle
        )
    }

    private fun onCollectionClick(name: String) {
        sendMarker(COLLECTION, name)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val VIEWED = 5
        const val INTERESTED = 6
        const val COLLECTION = 7
    }
}