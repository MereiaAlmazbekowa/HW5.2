package com.example.hw51.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw51.ui.adapter.CharacteristicAdapter
import com.example.hw51.data.model.Character
import com.example.hw51.databinding.FragmentCharacterBinding
import com.example.hw51.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


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

        viewModel.getCharactersPaging().observe(viewLifecycleOwner) { dataPaging ->
            lifecycleScope.launch {
                cartoonAdapter.submitData(dataPaging)
            }
        }
    }

    private fun setupRecyclerView() = with(binding.recyclerView) {
        layoutManager = LinearLayoutManager(context)
        adapter = cartoonAdapter
    }

    private fun onClicked(character: Character) {
        val action = CharacterFragmentDirections.actionCharacterFragmentToDetailFragment(character.id)
        findNavController().navigate(action)
    }
}