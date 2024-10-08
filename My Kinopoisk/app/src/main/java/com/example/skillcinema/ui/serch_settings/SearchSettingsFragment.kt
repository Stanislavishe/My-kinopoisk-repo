package com.example.skillcinema.ui.serch_settings

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
import com.example.skillcinema.databinding.FragmentSearchSettingsBinding
import com.example.skillcinema.ui.actor_fillscreen.navigateSave
import com.example.skillcinema.ui.search.SearchViewModel
import com.example.skillcinema.ui.search.SearchViewModelFactory
import com.google.android.material.slider.RangeSlider
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchSettingsFragment : Fragment() {

    @Inject
    lateinit var searchViewModelFactory: SearchViewModelFactory

    private var _binding: FragmentSearchSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels { searchViewModelFactory }
    private val searchSettingsViewModel: SearchSettingsViewModel by activityViewModels()

    private var searchIn: String? = null
    private var ratingFrom = 0
    private var ratingTo = 10
    private var yearFrom = 1000
    private var yearTo = 3000
    private var sortIn: String? = null
    private var countryList = emptyList<Int>()
    private var genreList = emptyList<Int>()

    private var viewedShow = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchIn = viewModel.type.value
        ratingFrom = viewModel.ratingFrom.value
        ratingTo = viewModel.ratingTo.value
        yearTo = viewModel.yearTo.value
        yearFrom = viewModel.yearFrom.value
        sortIn = viewModel.order.value
        countryList = viewModel.country.value
        genreList = viewModel.genre.value
        viewedShow = viewModel.showViewed.value
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            ratingSlider.values = listOf(ratingFrom.toFloat(), ratingTo.toFloat())

            countryTitle.setOnClickListener {
                findNavController().navigateSave(R.id.action_searchSettingsFragment_to_catchCountryFragment)
            }
            genreTitle.setOnClickListener {
                findNavController().navigateSave(R.id.action_searchSettingsFragment_to_catchGenreFragment)
            }
            yearTitle.setOnClickListener {
                findNavController().navigateSave(R.id.action_searchSettingsFragment_to_datePickerFragment)
            }
            swipe.setOnRefreshListener {
                refresh()
            }
            applyBtn.setOnClickListener {
                Log.d(
                    "SearchSettingsFragment",
                    "apply: $searchIn, $ratingFrom, $ratingTo, $yearFrom, $yearTo, $sortIn, $countryList, $genreList"
                )
                viewModel.applySettings(
                    searchIn!!,
                    ratingFrom, ratingTo, yearFrom, yearTo, sortIn!!, countryList, genreList, viewedShow
                )
                findNavController().navigateSave(R.id.action_searchSettingsFragment_to_search)
            }

            onShow()

            noShowText.setOnClickListener {
                viewedShow = !viewedShow
                onShow()
            }
            filmsFilterToggle.addOnButtonCheckedListener { _, checkedId, _ ->
                when (checkedId) {
                    R.id.all_btn -> searchIn = SearchViewModel.TYPE_DEFAULT
                    R.id.films_btn -> searchIn = FILM
                    R.id.serials_btn -> searchIn = TV_SERIES
                }
            }
            sortirFilterToggle.addOnButtonCheckedListener { _, checkedId, _ ->
                when (checkedId) {
                    R.id.date_btn -> sortIn = YEAR
                    R.id.popular_btn -> sortIn = NUM_VOTE
                    R.id.rating_btn -> sortIn = SearchViewModel.ORDER_DEFAULT
                }
            }
            ratingSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {}
                override fun onStopTrackingTouch(slider: RangeSlider) {
                    ratingFrom = slider.values.first().toInt()
                    ratingTo = slider.values.last().toInt()
                }
            })
            searchSettingsViewModel.countryFlow.onEach {
                if (it.isNotEmpty()) {
                    countryList = listOf(it.last().id)
                    country.text = it.last().country
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            searchSettingsViewModel.genreFlow.onEach {
                if (it.isNotEmpty()) {
                    genreList = listOf(it.last().id)
                    genre.text = it.last().genre
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            searchSettingsViewModel.yearFlow.onEach {
                observeYears(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            backButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        with(binding) {
            Log.d("SearchSettingsFragment", "$ratingFrom,,$ratingTo")
            ratingSlider.values = listOf(ratingFrom.toFloat(), ratingTo.toFloat())
            when (searchIn) {
                "ALL" -> allBtn.isChecked = true
                FILM -> filmsBtn.isChecked = true
                TV_SERIES -> serialsBtn.isChecked = true
            }
            when (sortIn) {
                "RATING" -> ratingBtn.isChecked = true
                NUM_VOTE -> popularBtn.isChecked = true
                YEAR -> dateBtn.isChecked = true
            }
        }
    }

    private fun observeYears(it: List<Int>){
        val text = StringBuilder()
        if(it[0] != 1000) {
            text.append("с ${it[0]}")
            text.append(" ")
        }
        if(it[1] != 3000) text.append("до ${it[1]}")
        if(it[0] == 1000 && it[1] == 3000) text.append("Любой")
        binding.year.text = text
        yearFrom = it[0]
        yearTo = it[1]
    }

    private fun refresh(){
        binding.swipe.isRefreshing = true
        searchIn = viewModel.type.value
        ratingFrom = viewModel.ratingFrom.value
        ratingTo = viewModel.ratingTo.value
        yearTo = viewModel.yearTo.value
        yearFrom = viewModel.yearFrom.value
        sortIn = viewModel.order.value
        countryList = viewModel.country.value
        genreList = viewModel.genre.value
        viewedShow = viewModel.showViewed.value
        onShow()
        binding.ratingSlider.values = listOf(ratingFrom.toFloat(), ratingTo.toFloat())
        binding.swipe.isRefreshing = false
    }

    private fun onShow(){
        if(viewedShow){
            binding.noShowBtn.setImageResource(R.drawable.no_show_icon)
        }
        else{
            binding.noShowBtn.setImageResource(R.drawable.no_show_blue_icon)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val FILM = "FILM"
        const val TV_SERIES = "TV_SERIES"
        const val NUM_VOTE = "NUM_VOTE"
        const val YEAR = "YEAR"
    }
}