package com.example.hw51.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw51.data.api.ApiService
import com.example.hw51.interfaces.OnClickItem
import com.example.hw51.ui.adapter.CharacteristicAdapter
import com.example.hw51.data.model.Character
import com.example.hw51.databinding.FragmentCharacterBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CharacterFragment : Fragment(), OnClickItem {

    private lateinit var adapter: CharacteristicAdapter
    private val viewModel: CharacterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCharacterBinding.inflate(inflater, container, false)

        adapter = CharacteristicAdapter(onClick = this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCharacters()
        viewModel.characters.observe(viewLifecycleOwner) { characters ->
            adapter.submitList(characters)
        }
    }

    override fun onClick(position: Character) {
        Toast.makeText(requireContext(), "Clicked: ${position.name}", Toast.LENGTH_SHORT).show()
    }
}