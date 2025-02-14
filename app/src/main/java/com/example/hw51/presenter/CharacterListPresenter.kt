package com.example.hw51.presenter

import com.example.hw51.interfaces.CharacterListView
import com.example.hw51.data.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CharacterListPresenter(private val view: CharacterListView, private val api: ApiService) {

    fun getCharacters() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = api.getCharacters()
                view.showCharacters(response.characters)
            } catch (e: Exception) {
                view.showError("Failed to load characters: ${e.message}")
            }
        }
    }
}