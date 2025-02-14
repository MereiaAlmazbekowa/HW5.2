package com.example.hw51.interfaces

import com.example.hw51.data.model.Character

interface CharacterListView {
    fun showCharacters(characters: List<Character>)
    fun showError(message: String)
}