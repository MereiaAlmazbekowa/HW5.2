package com.example.hw51.ui.fragment.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hw51.data.model.Character
import com.example.hw51.data.repository.CartoonRepository
import com.example.hw51.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: CartoonRepository
) : ViewModel() {

    private val _characterDetails = MutableLiveData<Character>()
    val characterDetails: LiveData<Character> get() = _characterDetails

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private var characterId: Int? = null

    fun setCharacterId(id: Int) {
        characterId = id
        fetchCharacterDetails()
    }

    private fun fetchCharacterDetails() {
        characterId?.let { id ->
            repository.getCharactersById(id).observeForever { resource ->
                when (resource) {
                    is Resource.Success -> _characterDetails.postValue(resource.data)
                    is Resource.Error -> _error.postValue(resource.message)
                    is Resource.Loading -> {}
                }
            }
        }
    }
}