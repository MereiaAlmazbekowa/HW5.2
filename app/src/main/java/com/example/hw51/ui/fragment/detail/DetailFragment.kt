package com.example.hw51.ui.fragment.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.hw51.R
import com.example.hw51.data.model.Character
import com.example.hw51.databinding.FragmentDetailBinding
import com.example.hw51.ui.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>(
    FragmentDetailBinding::inflate,
    DetailViewModel::class.java
) {

    private val viewModel by lazy {
        ViewModelProvider(this)[DetailViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val characterId = arguments?.getInt("id") ?: return
        viewModel.setCharacterId(characterId)

        viewModel.characterDetails.observe(viewLifecycleOwner) { character ->
            character?.let {
                bind(it)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                showToast(it)
            }
        }
    }

    private fun bind(character: Character) = with(binding) {
        tvName.text = character.name
        tvStatus.text = "${character.status} - ${character.species}"
        tvLastLocation.text = character.location.name
        tvGender.text = character.gender

        Glide.with(root.context)
            .load(character.image)
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.image)

        val circleDrawable = when (character.status) {
            "Alive" -> R.drawable.ic_circle_green
            "Dead" -> R.drawable.ic_circle_red
            else -> R.drawable.ic_cicrle_gray
        }
        image.setImageResource(circleDrawable)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}