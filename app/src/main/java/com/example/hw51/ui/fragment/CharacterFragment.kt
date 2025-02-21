package com.example.hw51.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw51.ui.adapter.CharacteristicAdapter
import com.example.hw51.data.model.Character
import com.example.hw51.databinding.FragmentCharacterBinding
import com.example.hw51.util.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CharacterFragment : Fragment() {

    private val binding by lazy {
        FragmentCharacterBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[CharacterViewModel::class.java]
    }

    private val cartoonAdapter by lazy {
        CharacteristicAdapter {
                character -> onClicked(character)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        viewModel.characters.observe(viewLifecycleOwner) { characters ->
            when (characters) {
                is Resource.Error -> {
                    showToast(characters.message)
//                    showProgressBar(false)
                }

                is Resource.Loading -> {}

                is Resource.Success -> {
//                    showProgressBar(false)
                    cartoonAdapter.submitList(characters.data)
                }
            }
        }
    }

    private fun setupRecyclerView() = with(binding.recyclerView) {
        layoutManager = LinearLayoutManager(context)
        adapter = cartoonAdapter
    }

//    private fun showProgressBar(isVisible: Boolean) = with(binding) {
//        if (isVisible) progressBar.visible() else progressBar.gone()
//    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun onClicked(character: Character) {
        val action = CharacterFragmentDirections.actionCharacterFragmentToDetailFragment(character.id)
        findNavController().navigate(action)
    }
}