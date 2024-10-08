package com.example.skillcinema.ui.actor_fillscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.skillcinema.R
import com.example.skillcinema.data.models.staffs.SingleStaff
import com.example.skillcinema.databinding.FragmentActorFullscreenBinding
import com.example.skillcinema.ui.film_fullscreen.FilmFullscreenFragment.Companion.ACTORID
import com.example.skillcinema.ui.film_fullscreen.FilmFullscreenFragment.Companion.IMAGEID
import com.example.skillcinema.ui.films_list.FilmListFragment.Companion.SINGLEID
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ActorFullscreenFragment : Fragment() {

    @Inject
    lateinit var actorFullscreenViewModelFactory: ActorFullscreenViewModelFactory

    private var _binding: FragmentActorFullscreenBinding? = null
    private val binding get() = _binding!!

    private val actorFullscreenViewModel: ActorFullscreenViewModel by viewModels { actorFullscreenViewModelFactory }

    private var staffId: Int? = null

    private val adapter by lazy {
        BestsAdapter(actorFullscreenViewModel.filmsUseCase, lifecycleScope) {
            onFilmClick(
                it.kinopoiskId
            )
        }
    }

    private fun onFilmClick(id: Int) {
        val bundle = Bundle().apply {
            putInt(SINGLEID, id)
        }
        findNavController().navigateSave(R.id.action_staffFullscreen_to_filmFullscreen, bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            staffId = it.getInt(ACTORID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorFullscreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        actorFullscreenViewModel.loadStaff(staffId!!)
        binding.swipe.setOnRefreshListener {
            actorFullscreenViewModel.refresh(staffId!!)
        }
        actorFullscreenViewModel.isLoading.onEach {
            binding.swipe.isRefreshing = it
        }.launchIn(lifecycleScope)

        actorFullscreenViewModel.staff.onEach { list ->
            loadStuff(list)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    private fun loadStuff(list: List<SingleStaff?>){
        if (list.isNotEmpty()) {
            val staff = list.last()
            if (staff != null) {
                with(binding) {
                    Glide
                        .with(actorPhoto.context)
                        .load(staff.posterUrl)
                        .into(actorPhoto)

                    setupAdapters(staff)

                    viewLifecycleOwner.lifecycleScope.launch {
                        adapter.submitList(staff.films)
                    }

                    filmCount.text = getString(R.string.s_films, staff.films.size.toString())

                    actorPhoto.setOnClickListener { onActorPhotoClick(staff) }
                    onListBut.setOnClickListener { onListBtnClick(staff) }
                }
            }
        }
    }

    private fun setupAdapters(staff: SingleStaff){
        with(binding){
            actorName.text = staff.nameRu ?: staff.nameEn ?: ""
            actorDescr.text = staff.profession ?: ""
            bestList.adapter = adapter
        }
    }

    private fun onActorPhotoClick(staff: SingleStaff){
        val bundle = Bundle().apply {
            putString(IMAGEID, staff.posterUrl)
        }
        findNavController().navigateSave(
            R.id.action_staffFullscreen_to_photoFullscreen,
            bundle
        )
    }

    private fun onListBtnClick(staff: SingleStaff){
        val bundle = Bundle().apply {
            putInt(FILMOGRAPHYID, staff.personId)
        }
        findNavController().navigateSave(
            R.id.action_staffFullscreen_to_filmography,
            bundle
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val FILMOGRAPHYID = "FILMOGRAPHYID"
    }
}

fun NavController.navigateSave(@IdRes resId: Int, args: Bundle? = null) {
    val destinationId = currentDestination?.getAction(resId)?.destinationId
    currentDestination?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }
        if (destinationId != 0 && destinationId != null) {
            currentNode?.findNode(destinationId)?.let { navigate(resId, args) }
        }
    }
}