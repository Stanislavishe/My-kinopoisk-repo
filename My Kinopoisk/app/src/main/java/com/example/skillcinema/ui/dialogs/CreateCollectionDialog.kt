package com.example.skillcinema.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentCollectionDialogBinding
import com.example.skillcinema.entity.Collection
import com.example.skillcinema.ui.profile.ProfileViewModel
import com.example.skillcinema.ui.profile.ProfileViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateCollectionDialog : DialogFragment() {
    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val viewModel by activityViewModels<ProfileViewModel> { profileViewModelFactory }

    private var _binding: FragmentCollectionDialogBinding? = null
    private val binding get() = _binding!!

    private val iconsFlow = MutableStateFlow(R.drawable.home_icon)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollectionDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            readyBtn.setOnClickListener {
                viewModel.setCollection(
                    Collection(
                        name = inputEdit.text.toString(),
                        icon = iconsFlow.value
                    )
                )
                lifecycleScope.launch {
                    viewModel.isError.collect {
                        readyBtn.isEnabled = !it
                        if (it) {
                            inputLay.error = "Такая коллекция уже существует"
                        } else {
                            inputLay.error = null
                            this@CreateCollectionDialog.dismissNow()
                        }
                    }
                }
            }
            home.setOnClickListener { setIcon(R.drawable.home_icon) }
            heart.setOnClickListener { setIcon(R.drawable.heart_icon) }
            lock.setOnClickListener { setIcon(R.drawable.lock_icon) }
            cloud.setOnClickListener { setIcon(R.drawable.cloud_icon) }
            star.setOnClickListener { setIcon(R.drawable.star_icon) }
            bookmark.setOnClickListener { setIcon(R.drawable.bookmark_icon) }
            icons.setOnClickListener {
                chaIcons.visibility = View.VISIBLE
            }
            inputEdit.doOnTextChanged { text, _, _, _ ->
                inputLay.error = null
                readyBtn.isEnabled = text?.length != null
            }
        }
    }

    private fun setIcon(id: Int) {
        binding.chaIcons.visibility = View.GONE
        binding.icons.setImageResource(id)
        iconsFlow.value = id
    }

    companion object {
        const val TAG = "CreateCollectionDialog"
    }
}