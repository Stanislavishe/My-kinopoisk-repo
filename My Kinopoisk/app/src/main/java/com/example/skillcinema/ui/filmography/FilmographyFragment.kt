package com.example.skillcinema.ui.filmography

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.data.models.staffs.Filmography
import com.example.skillcinema.databinding.FragmentFilmographyBinding
import com.example.skillcinema.ui.actor_fillscreen.ActorFullscreenFragment.Companion.FILMOGRAPHYID
import com.example.skillcinema.ui.actor_fillscreen.navigateSave
import com.example.skillcinema.ui.films_list.FilmListFragment.Companion.SINGLEID
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilmographyFragment : Fragment() {

    @Inject
    lateinit var filmographyViewModelFactory: FilmographyViewModelFactory

    private var _binding: FragmentFilmographyBinding? = null
    private val binding get() = _binding!!

    private var staffId: Int? = null
    private val professionalList = mutableListOf<String>()

    private val filmographyViewModel: FilmographyViewModel by viewModels { filmographyViewModelFactory }

    private val adapter by lazy {
        FilmographyAdapter(filmographyViewModel.filmsUseCase, viewLifecycleOwner.lifecycleScope) {
            onFilmClick(it.kinopoiskId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            staffId = it.getInt(FILMOGRAPHYID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmographyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        filmographyViewModel.loadStaff(staffId!!)
        binding.filmList.adapter = adapter
        filmographyViewModel.staff.onEach { list ->
            if (list.isNotEmpty()) {
                val staff = list.last()
                if(staff != null) {
                    val films = staff.films
                    showChips(films)
                    firstChecked(films)
                    binding.chipsGroup.setOnCheckedStateChangeListener { group, _ ->
                        checkChip(group, films)
                    }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun checkChip(group: ChipGroup, films: List<Filmography>){
        with(binding){
            when (group.checkedChipId) {
                actorChip.id -> loadFilmography(films, ACTOR)
                actorVoiceChip.id -> loadFilmography(films, VOICE_FEMALE)
                directorChip.id -> loadFilmography(films, DIRECTOR)
                writerChip.id -> loadFilmography(films, WRITER)
                producerChip.id -> loadFilmography(films, PRODUCER)
                else -> {}
            }
        }

    }

    private fun loadFilmography(films: List<Filmography>, key: String) {
        lifecycleScope.launch {
            adapter.submitList(films.filter { it.professionKey == key })
        }
    }

    private fun onFilmClick(id: Int) {
        val bundle = Bundle().apply { putInt(SINGLEID, id) }
        findNavController().navigateSave(R.id.action_filmography_to_filmFullscreen, bundle)
    }

    private fun firstChecked(films: List<Filmography>) {
        when (professionalList.distinct().first()) {
            ACTOR -> binding.actorChip.isChecked = true
            DIRECTOR -> binding.directorChip.isChecked = true
            PRODUCER -> binding.producerChip.isChecked = true
            WRITER -> binding.writerChip.isChecked = true
            VOICE_FEMALE -> binding.actorVoiceChip.isChecked = true
        }
        checkChip(binding.chipsGroup, films)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showChips(films: List<Filmography>) {
        professionalList.clear()
        films.forEach {
            when (it.professionKey) {
                ACTOR -> {
                    professionalList.add(ACTOR)
                    binding.actorChip.visibility = View.VISIBLE
                }

                DIRECTOR -> {
                    professionalList.add(DIRECTOR)
                    binding.directorChip.visibility = View.VISIBLE
                }

                PRODUCER -> {
                    professionalList.add(PRODUCER)
                    binding.producerChip.visibility = View.VISIBLE
                }

                WRITER -> {
                    professionalList.add(WRITER)
                    binding.writerChip.visibility = View.VISIBLE
                }

                VOICE_FEMALE -> {
                    professionalList.add(VOICE_FEMALE)
                    binding.actorVoiceChip.visibility = View.VISIBLE
                }

                else -> {}
            }
        }
    }

    companion object {
        const val DIRECTOR = "DIRECTOR"
        const val PRODUCER = "PRODUCER"
        const val ACTOR = "ACTOR"
        const val WRITER = "WRITER"
        const val VOICE_FEMALE = "VOICE_FEMALE"
    }
}