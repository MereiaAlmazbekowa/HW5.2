package com.example.hw51.ui.fragment.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.hw51.data.api.ApiService
import com.example.hw51.data.model.Character
import com.example.hw51.data.model.episode.Episode
import com.example.hw51.data.paging.CharactersPagingSource
import com.example.hw51.room.CharacterDao
import com.example.hw51.room.CharacterEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val api: ApiService,
    private val characterDao: CharacterDao,
    private val pagingSource: CharactersPagingSource
) : ViewModel() {

    private val _episodeName = MutableLiveData<String>()
    val episodeName: LiveData<String> get() = _episodeName

    private val episodeCache = mutableMapOf<String, String>()

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

    fun getViewedCharacters(): LiveData<List<CharacterEntity>> {
        return characterDao.getAllViewedCharacters()
    }


}