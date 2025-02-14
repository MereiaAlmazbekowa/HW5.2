package com.example.hw51.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw51.data.api.ApiService
import com.example.hw51.presenter.CharacterListPresenter
import com.example.hw51.interfaces.CharacterListView
import com.example.hw51.interfaces.OnClickItem
import com.example.hw51.ui.adapter.CharacteristicAdapter
import com.example.hw51.data.model.Character
import com.example.hw51.databinding.FragmentCharacterBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CharacterFragment : Fragment(), CharacterListView, OnClickItem {

    private lateinit var presenter: CharacterListPresenter
    private lateinit var adapter: CharacteristicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCharacterBinding.inflate(inflater, container, false)

        adapter = CharacteristicAdapter(onClick = this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        val api = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        presenter = CharacterListPresenter(this, api)
        presenter.getCharacters()

        return binding.root
    }

    override fun showCharacters(characters: List<Character>) {
        adapter.submitList(characters)
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(position: Character) {
        Toast.makeText(requireContext(), "Clicked: ${position.name}", Toast.LENGTH_SHORT).show()
    }
}