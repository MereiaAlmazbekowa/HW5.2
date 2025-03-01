package com.example.hw51.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.hw51.data.api.ApiService
import com.example.hw51.data.model.BaseResponse
import com.example.hw51.data.model.Character
import com.example.hw51.data.model.episode.Episode
import com.example.hw51.data.paging.CharactersPagingSource
import com.example.hw51.data.repository.CartoonRepository
import com.example.hw51.room.CharacterDao
import com.example.hw51.room.CharacterEntity
import com.example.hw51.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val api: ApiService,
    private val characterDao: CharacterDao,
    private val pagingSource: CharactersPagingSource
) : ViewModel() {

    private val _charactersData = MutableLiveData<List<Character>>()
    val charactersData: LiveData<List<Character>> get() = _charactersData

    private val _errorData = MutableLiveData<String>()
    val errorData: LiveData<String> get() = _errorData

    private val _episodeName = MutableLiveData<String>()
    val episodeName: LiveData<String> get() = _episodeName

    private val episodeCache = mutableMapOf<String, String>()

    fun getCharacters() {
        api.getCharacters().enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    response.body()?.characters?.let {
                        _charactersData.postValue(it)
                    } ?: run {
                        _errorData.postValue("No characters found")
                    }
                } else {
                    _errorData.postValue("Failed to fetch characters: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                _errorData.postValue(t.localizedMessage ?: "Unknown error")
            }
        })
    }

    fun getCharactersPaging(): LiveData<PagingData<Character>>{
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                initialLoadSize = 20 * 2
            ),
            pagingSourceFactory = {pagingSource}
        ).liveData
    }

    fun getEpisodeNameForCharacter(episodeUrl: String, callback: (String) -> Unit) {
        episodeCache[episodeUrl]?.let { cachedName ->
            callback(cachedName)
            return
        }

        api.getEpisodeName(episodeUrl).enqueue(object : Callback<Episode> {
            override fun onResponse(call: Call<Episode>, response: Response<Episode>) {
                val fetchedName = if (response.isSuccessful) {
                    response.body()?.name ?: "???"
                } else {
                    "???"
                }
                episodeCache[episodeUrl] = fetchedName
                callback(fetchedName)
            }

            override fun onFailure(call: Call<Episode>, t: Throwable) {
                callback("???")
            }
        })
    }


    fun saveViewedCharacter(character: Character) {
        character.episode?.firstOrNull()?.let { episodeUrl ->
            getEpisodeNameForCharacter(episodeUrl) { episodeName ->
                viewModelScope.launch(Dispatchers.IO) {
                    val entity = CharacterEntity(
                        id = character.id,
                        name = character.name,
                        status = character.status ?: "Unknown",
                        species = character.species ?: "Unknown",
                        gender = character.gender ?: "Unknown",
                        location = character.location?.name ?: "Unknown",
                        firstEpisodeName = episodeName,
                        origin = character.origin?.name ?: "Unknown",
                        imageBase64 = character.image?.let { ImageUtils.urlToBase64(it) }
                    )

                    characterDao.insertCharacter(entity)
                }
            }
        }
    }


    fun getViewedCharacters(): LiveData<List<CharacterEntity>> {
        return characterDao.getAllViewedCharacters()
    }


}